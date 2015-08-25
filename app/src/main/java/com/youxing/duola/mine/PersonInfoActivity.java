package com.youxing.duola.mine;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.common.app.Constants;
import com.youxing.common.model.Account;
import com.youxing.common.model.BaseModel;
import com.youxing.common.model.Child;
import com.youxing.common.model.UploadImageModel;
import com.youxing.common.services.account.AccountService;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.common.utils.Log;
import com.youxing.common.utils.UnitTools;
import com.youxing.common.views.CircularImage;
import com.youxing.duola.R;
import com.youxing.duola.app.DLActivity;
import com.youxing.duola.model.AccountModel;
import com.youxing.duola.utils.PhotoPicker;
import com.youxing.duola.views.SectionView;
import com.youxing.duola.views.SimpleListItem;
import com.youxing.duola.views.StepperView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Jun Deng on 15/8/24.
 */
public class PersonInfoActivity extends DLActivity implements StepperView.OnNumberChangedListener, AdapterView.OnItemClickListener {

    private static final int REQUEST_IMAGE_CAMERA = 1;
    private static final int REQUEST_IMAGE_PHONE = 2;

    private ListView listView;
    private Adapter adapter;

    private Account account;
    private PhotoPicker photoPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        account = AccountService.instance().account();

        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        adapter = new Adapter();
        listView.setAdapter(adapter);

        requestData();
    }

    public void requestData() {
        showLoading();

        HttpService.get(Constants.domain() + "/user", null, CacheType.DISABLE, AccountModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(BaseModel response) {
                dismissLoading();

                AccountModel model = (AccountModel) response;
                PersonInfoActivity.this.account = model.getData();
                AccountService.instance().dispatchAccountChanged(model.getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissLoading();
                showDialog(PersonInfoActivity.this, error.getErrmsg(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroupStyleAdapter.IndexPath indexPath = adapter.getIndexForPosition(position);
        int section = indexPath.section;
        int row = indexPath.row;
        if (section == 0) {
            if (row == 0) {
                pickPhoto();
            }
        }
    }

    private void pickPhoto() {
        if (photoPicker == null) {
            photoPicker = new PhotoPicker(PersonInfoActivity.this);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonInfoActivity.this);
        builder.setItems(new String[]{"拍照", "从手机相册里选择"}, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // take a new photo
                        photoPicker.doTakePhoto();
                        break;

                    case 1:
                        // pick a photo from gallery
                        photoPicker.doPickPhotoFromGallery();
                        break;
                }
            }
        });
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String strImgPath;
            switch (requestCode) {
                case PhotoPicker.REQUEST_CODE_CAMERA:
                    strImgPath = photoPicker.strImgPath();
                    if (!TextUtils.isEmpty(strImgPath)) {
                        File f = new File(strImgPath);
                        if (!f.exists()) {
                            strImgPath = photoPicker.parseImgPath(data);
                        }

                    } else {
                        strImgPath = photoPicker.parseImgPath(data);
                    }
                    if (!TextUtils.isEmpty(strImgPath)) {
                        photoPicker.doCropPhoto();
                    }
                    break;
                case PhotoPicker.REQUEST_CODE_PHOTO_PICKED:
                    if (Build.VERSION.SDK_INT < 19) {
                        strImgPath = photoPicker.parseImgPath(data);
                        if (!TextUtils.isEmpty(strImgPath)) {
                            // sharePhoto.setImageBitmap(parseThumbnail(strImgPath));
//                            addThumb(parseThumbnail(strImgPath));
                            requestUploadImage(new File(strImgPath));
                        }
                    } else {
                        if (data != null) {
                            // sharePhoto
                            // .setImageBitmap(parseThumbnail(data.getData()));
//                            addThumb(parseThumbnail(data.getData()));
                            strImgPath = photoPicker.getPath(PersonInfoActivity.this, data.getData());
                            requestUploadImage(new File(strImgPath));
                        }
                    }
                    break;
            }
        }
    }

    private void requestUploadImage(File file) {
//        showLoading();

        HttpService.uploadImage(file, new RequestHandler() {
            @Override
            public void onRequestFinish(BaseModel response) {
                UploadImageModel model = (UploadImageModel) response;
                requestUpdateAvatar(model.getData().getPath());
            }

            @Override
            public void onRequestFailed(BaseModel error) {
//                dismissLoading();
                showDialog(PersonInfoActivity.this, error.getErrmsg(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            }
        });
    }

    private void requestUpdateAvatar(String url) {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("avatar", url));
        HttpService.post(Constants.domain() + "/user/avatar", params, AccountModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(BaseModel response) {
                dismissLoading();

                AccountModel model = (AccountModel) response;
                PersonInfoActivity.this.account = model.getData();
                AccountService.instance().dispatchAccountChanged(model.getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissLoading();
                showDialog(PersonInfoActivity.this, error.getErrmsg(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public void onNumberChanged(StepperView stepper) {

    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(PersonInfoActivity.this);
        }

        @Override
        public int getSectionCount() {
            if (account.getChildren().size() == 0) {
                return 4;
            }
            return 3 + account.getChildren().size();
        }

        @Override
        public int getCountInSection(int section) {
            if (section == 0) {
                return 3;
            } else if (section == 1) {
                return 2;
            } else if (section == getSectionCount() - 1) {
                return 0;
            }

            if (account.getChildren().size() == 0) {
                return 0;
            }
            return 3;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            View view = null;
            if (section == 0 && row == 0) {
                view = LayoutInflater.from(PersonInfoActivity.this).inflate(R.layout.layout_personinfo_avatar_item, null);
                TextView title = (TextView) view.findViewById(R.id.title);
                title.setText("头像");
                CircularImage avatar = (CircularImage) view.findViewById(R.id.avatar);
                avatar.setDefaultImageResId(R.drawable.ic_default_avatar);
                avatar.setImageUrl(account.getAvatar());

            } else {
                SimpleListItem item = SimpleListItem.create(PersonInfoActivity.this);
                item.setShowArrow(true);
                if (section == 0) {
                    if (row == 1) {
                        item.setTitle("昵称");
                        item.setSubTitle(account.getNickName());

                    } else if (row == 2) {
                        item.setTitle("手机号");
                        item.setSubTitle(account.getMobile());
                        item.setShowArrow(false);
                    }

                } else if (section == 1) {
                    if (row == 0) {
                        item.setTitle("性别");
                        item.setSubTitle(account.getSex());
                    } else {
                        item.setTitle("常住地");
                        item.setSubTitle(account.getAddress());
                    }

                } else {
                    Child child = account.getChildren().get(section - 2);
                    if (row == 0) {
                        item.setTitle("孩子昵称");
                        item.setSubTitle(child.getName());
                    } else if (row == 1) {
                        item.setTitle("性别");
                        item.setSubTitle(child.getSex());
                    } else {
                        item.setTitle("生日");
                        item.setSubTitle(child.getBirthday());
                    }
                }
                view = item;
            }
            return view;
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            if (section == 0) {
                SectionView sectionView = SectionView.create(PersonInfoActivity.this);
                sectionView.setTitle("个人信息");
                return sectionView;

            } else if (section == 2) {
                View view = LayoutInflater.from(PersonInfoActivity.this).inflate(R.layout.layout_personinfo_section_child, null);
                TextView title = (TextView) view.findViewById(R.id.title);
                title.setText("孩子信息（" + account.getChildren().size() + "个）");
                StepperView stepper = (StepperView) view.findViewById(R.id.stepper);
                stepper.setMin(0);
                stepper.setMax(5);
                stepper.setNumber(account.getChildren().size());
                stepper.setListener(PersonInfoActivity.this);
                return view;

            } else if (section == getSectionCount() - 1) {
                LinearLayout ll = new LinearLayout(PersonInfoActivity.this);
                int padding = UnitTools.dip2px(PersonInfoActivity.this, 20);
                ll.setPadding(padding, padding, padding, padding);
                Button payBtn = new Button(PersonInfoActivity.this);
                payBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                payBtn.setText("退出登录");
                payBtn.setTextSize(18);
                payBtn.setTextColor(getResources().getColor(R.color.white));
                payBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AccountService.instance().dispatchAccountChanged(null);
                        finish();
                    }
                });
                padding = UnitTools.dip2px(PersonInfoActivity.this, 10);
                payBtn.setPadding(padding, padding, padding, padding);
                payBtn.setBackgroundResource(R.drawable.btn_shape_green);
                ll.addView(payBtn);
                return ll;
            }
            return super.getViewForSection(convertView, parent, section);
        }
    }

}

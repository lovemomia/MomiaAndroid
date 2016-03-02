package com.youxing.duola.mine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;

import com.alibaba.fastjson.JSON;
import com.lling.photopicker.PhotoPickerActivity;
import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.common.app.Constants;
import com.youxing.common.app.Enviroment;
import com.youxing.common.model.BaseModel;
import com.youxing.common.model.UploadImageModel;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.common.utils.UnitTools;
import com.youxing.duola.R;
import com.youxing.duola.app.SGActivity;
import com.youxing.duola.course.BookedCourseListFragment;
import com.youxing.duola.mine.views.AddReviewContentItem;
import com.youxing.duola.mine.views.AddReviewRatingItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun Deng on 16/2/26.
 */
public class AddReviewActivity extends SGActivity implements RatingBar.OnRatingBarChangeListener,
        View.OnClickListener, TextWatcher {

    private final static int MENU_ID_SUBMIT = 1;
    private final static int REQUEST_CODE_PICK_PHOTO = 1;

    private String id;
    private String bookingId;

    private Adapter adapter;

    private int totalScore;
    private int teacherScore;
    private int envScore;
    private String content;
    private List<UploadImage> uploadPhotos;

    private boolean isSubmitting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        id = getIntent().getData().getQueryParameter("id");
        bookingId = getIntent().getData().getQueryParameter("bookingId");

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new Adapter(this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        int tag = (int)ratingBar.getTag();
        if (tag == 0) {
            totalScore = (int) rating;

        } else if (tag == 1) {
            teacherScore = (int) rating;

        } else {
            envScore = (int) rating;
        }
    }

    @Override
    public void onClick(View v) {
        pickPhoto();
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, MENU_ID_SUBMIT, 0, "发布").setShowAsAction(MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_ID_SUBMIT) {
            submit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void submit() {
        if (isSubmitting) {
            return;
        }

        if (check()) {
            isSubmitting = true;
            if (uploadPhotos != null && uploadPhotos.size() > 0) {
                showLoadingDialog(this);
                requestUploadImage(uploadPhotos.get(0));
            } else {
                showLoadingDialog(this);
                requestSubmit();
            }
        }
    }

    private boolean check() {
        if (totalScore == 0) {
            showDialog(this, "您还没有选择总体评分！");
            return false;
        }
        if (teacherScore == 0) {
            showDialog(this, "您还没有选择老师评分！");
            return false;
        }
        if (envScore == 0) {
            showDialog(this, "您还没有选择环境评分！");
            return false;
        }
        if (TextUtils.isEmpty(content)) {
            showDialog(this, "您还没有填写内容！");
            return false;
        }
        return true;
    }

    private void pickPhoto() {
        Intent intent = new Intent(AddReviewActivity.this, PhotoPickerActivity.class);
        intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
        intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);
        intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, 9);
        startActivityForResult(intent, REQUEST_CODE_PICK_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_PHOTO) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                if (result.size() > 0) {
                    if (this.uploadPhotos == null) {
                        this.uploadPhotos = new ArrayList<UploadImage>();
                    }
                    for (String path : result) {
                        UploadImage ui = new UploadImage();
                        ui.filePath = path;
                        this.uploadPhotos.add(ui);
                    }
                    this.adapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void showPhoto(ImageView photo, String picturePath) {
        if(picturePath.equals(""))
            return;
        // 缩放图片, width, height 按相同比例缩放图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        // options 设为true时，构造出的bitmap没有图片，只有一些长宽等配置信息，但比较快，设为false时，才有图片
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
        int scale = (int)( options.outWidth / (float)300);
        if(scale <= 0)
            scale = 1;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(picturePath, options);

        photo.setImageBitmap(bitmap);
        photo.setMaxHeight(350);
    }

    /**
     *
     * @param file
     * @return true:全部上传成功
     */
    private void requestUploadImage(final UploadImage file) {
        file.status = 1;

        HttpService.uploadImage(new File(file.filePath), new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                UploadImageModel uploadImageModel = (UploadImageModel) response;
                file.url = uploadImageModel.getData().getPath();
                file.status = 2;
                for (UploadImage file : uploadPhotos) {
                    if (file.status <= 0) {
                        requestUploadImage(file);
                        return;
                    }
                }

                requestSubmit();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(AddReviewActivity.this, error.getErrmsg());
                file.status = -1;
            }
        });
    }

    private void requestSubmit() {
        AddReview ar = new AddReview();
        ar.courseId = Long.valueOf(id);
        ar.bookingId = Long.valueOf(bookingId);
        ar.star = totalScore;
        ar.teacher = teacherScore;
        ar.enviroment = envScore;
        ar.content = content;
        if (uploadPhotos != null) {
            ar.imgs = new ArrayList<String>();
            for (UploadImage file : uploadPhotos) {
                ar.imgs.add(file.url);
            }
        }

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("comment", JSON.toJSONString(ar)));

        HttpService.post(Constants.domain() + "/course/comment", params, BaseModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();
                showDialog(AddReviewActivity.this, "发布成功！", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                        sendBroadcast(new Intent(BookedCourseListFragment.INTENT_ACTION_DATA_CHANGE));
                    }
                });

                isSubmitting = false;
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                showDialog(AddReviewActivity.this, error.getErrmsg());
                isSubmitting = false;
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        content = s.toString();
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    class Adapter extends GroupStyleAdapter {


        public Adapter(Context context) {
            super(context);
        }

        @Override
        public int getSectionCount() {
            return 2;
        }

        @Override
        public int getCountInSection(int section) {
            if (section == 0) {
                return 3;
            }
            return 1;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            View listItem = null;
            if (section == 0) {
                AddReviewRatingItem ratingItem = AddReviewRatingItem.create(AddReviewActivity.this, row);
                ratingItem.getRatingBar().setTag(row);
                if (row == 0) {
                    ratingItem.getTitleTv().setText("总体");
                    ratingItem.getRatingBar().setOnRatingBarChangeListener(AddReviewActivity.this);
                    ratingItem.getRatingBar().setRating(totalScore);

                } else if (row == 1) {
                    ratingItem.getTitleTv().setText("老师");
                    ratingItem.getRatingBar().setOnRatingBarChangeListener(AddReviewActivity.this);
                    ratingItem.getRatingBar().setRating(teacherScore);

                } else {
                    ratingItem.getTitleTv().setText("环境");
                    ratingItem.getRatingBar().setOnRatingBarChangeListener(AddReviewActivity.this);
                    ratingItem.getRatingBar().setRating(envScore);
                }
                listItem = ratingItem;

            } else {
                AddReviewContentItem contentItem = AddReviewContentItem.create(AddReviewActivity.this);
                contentItem.getContentEt().setText(content);
                contentItem.getContentEt().addTextChangedListener(AddReviewActivity.this);
                contentItem.getAddView().setOnClickListener(AddReviewActivity.this);

                int margin = UnitTools.dip2px(AddReviewActivity.this, 10);
                int width = (Enviroment.screenWidth(AddReviewActivity.this) - 5 * margin) / 4;

                contentItem.getAddView().getLayoutParams().width = width;
                contentItem.getAddView().getLayoutParams().height = width;

                if (uploadPhotos != null && uploadPhotos.size() > 0) {
                    if (uploadPhotos.size() == 9) {
                        contentItem.getGridLay().removeAllViews();
                    }
                    for (int i = uploadPhotos.size() - 1; i >= 0 ; i--) {
                        String path = uploadPhotos.get(i).filePath;
                        ImageView iv = new ImageView(AddReviewActivity.this);
                        GridLayout.LayoutParams lp = new GridLayout.LayoutParams();

                        lp.height = width;
                        lp.width = lp.height;

                        lp.setMargins(margin/2, margin/2, margin/2, margin/2);
                        iv.setLayoutParams(lp);
                        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        contentItem.getGridLay().addView(iv, 0);
                        showPhoto(iv, path);
                    }
                }
                listItem = contentItem;
            }
            return listItem;
        }
    }

    class AddReview {
        long courseId;
        long bookingId;
        int star;
        int teacher;
        int enviroment;
        String content;
        List<String> imgs;

        public long getCourseId() {
            return courseId;
        }

        public void setCourseId(long courseId) {
            this.courseId = courseId;
        }

        public long getBookingId() {
            return bookingId;
        }

        public void setBookingId(long bookingId) {
            this.bookingId = bookingId;
        }

        public int getStar() {
            return star;
        }

        public void setStar(int star) {
            this.star = star;
        }

        public int getTeacher() {
            return teacher;
        }

        public void setTeacher(int teacher) {
            this.teacher = teacher;
        }

        public int getEnviroment() {
            return enviroment;
        }

        public void setEnviroment(int enviroment) {
            this.enviroment = enviroment;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<String> getImgs() {
            return imgs;
        }

        public void setImgs(List<String> imgs) {
            this.imgs = imgs;
        }
    }

    class UploadImage {
        String filePath;
        int status; // -1.failed 0.pending 1.uploading 2.finish
        String url;
    }
}

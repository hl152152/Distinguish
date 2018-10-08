package com.gpm.dunqi.com.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.BankCardParams;
import com.baidu.ocr.sdk.model.BankCardResult;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.sdk.model.OcrRequestParams;
import com.baidu.ocr.sdk.model.OcrResponseResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baidu.ocr.ui.camera.CameraNativeHelper;
import com.baidu.ocr.ui.camera.CameraView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * _oo0oo_
 * o8888888o
 * 88" . "88
 * (| -_- |)
 * 0\  =  /0
 * ___/`---'\___
 * .' \\|     |// '.
 * / \\|||  :  |||// \
 * / _||||| -卍-|||||- \
 * |   | \\\  -  /// |   |
 * | \_|  ''\---/''  |_/ |
 * \  .-\__  '-'  ___/-. /
 * ___'. .'  /--.--\  `. .'___
 * ."" '<  `.___\_<|>_/___.' >' "".
 * | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * \  \ `_.   \_ __\ /__ _/   .-` /  /
 * =====`-.____`.___ \_____/___.-`___.-'=====
 * `=---='
 * 佛祖保佑        永无BUG
 * 佛曰:
 * 程序园里程序天，程序天里程序员；
 * 程序猿人写程序，又拿程序换肉钱。
 * 肉饱继续桌前坐，饱暖还是桌前眠；
 * 半迷半醒日复日，码上码下年复年。
 * 但愿叱咤互联世，不愿搬砖码当前；
 * 诸葛周瑜算世事，我来算出得加钱。
 * 别人笑我忒直男，我笑自己太像猿；
 * 但见重庆府国内，处处地地程序员。
 * Created by HCJ
 * ${DATA}
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE_CAMERA = 102;
    private static final int REQUEST_CODE_DRIVING_LICENSE = 103;
    private static final int REQUEST_CODE_VEHICLE_LICENSE = 104;

    @BindView(R.id.id_card_front_button_auto)
    Button idCardFrontButtonAuto;
    @BindView(R.id.id_card_back_button_auto)
    Button idCardBackButtonAuto;
    public Intent intent;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.id_card_front_button)
    Button idCardFrontButton;
    @BindView(R.id.id_card_back_button)
    Button idCardBackButton;
    @BindView(R.id.credit_card_button)
    Button creditCardButton;
    @BindView(R.id.driving_card_button)
    Button drivingCardButton;
    @BindView(R.id.vehicle_card_button)
    Button vehicleCardButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // 初始化
        initAccessTokenWithAkSk();
        checkPermission(); //权限
    }

    /**
     * 判断相关权限问题
     */
    public void checkPermission() {
//        int checkINSTALL = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.REQUEST_INSTALL_PACKAGES);
        int checkSelfPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (checkSelfPermission == PackageManager.PERMISSION_GRANTED) {     //允许  0,  询问  -1
            //已经获取到权限  获取用户媒体资源
//            checkUpdate();
        } else {
            //没有拿到权限  是否需要在第二次请求权限的情况下
            // 先自定义弹框说明 同意后在请求系统权限(就是是否需要自定义DialogActivity)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1514);
        }
    }


    @OnClick({R.id.credit_card_button, R.id.driving_card_button, R.id.vehicle_card_button, R.id.id_card_front_button, R.id.id_card_back_button, R.id.id_card_front_button_auto, R.id.id_card_back_button_auto})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.credit_card_button://银行卡

                intent = new Intent(MainActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_BANK_CARD);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);

                break;

            case R.id.driving_card_button://驾驶证

                intent = new Intent(MainActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_DRIVING_LICENSE);

                break;

            case R.id.vehicle_card_button://行驶证

                intent = new Intent(MainActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_VEHICLE_LICENSE);

                break;


            case R.id.id_card_front_button://身份证正面(手动识别)

                intent = new Intent(MainActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);

                break;
            case R.id.id_card_back_button://身份证反面(手动识别)

                intent = new Intent(MainActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);

                break;

            case R.id.id_card_front_button_auto://身份证正面(自动识别)

                intent = new Intent(MainActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE, true);
                // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
                // 请手动使用CameraNativeHelper初始化和释放模型
                // 推荐这样做，可以避免一些activity切换导致的不必要的异常
                intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL, true);
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);

                break;
            case R.id.id_card_back_button_auto://身份证反面(自动识别)


                intent = new Intent(MainActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE, true);
                // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
                // 请手动使用CameraNativeHelper初始化和释放模型
                // 推荐这样做，可以避免一些activity切换导致的不必要的异常
                intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL, true);
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);

                break;
        }
    }


    private void initAccessTokenWithAkSk() {
        OCR.getInstance().initAccessTokenWithAkSk(
                new OnResultListener<AccessToken>() {
                    @Override
                    public void onResult(AccessToken result) {
                        // 本地自动识别需要初始化
                        initLicense();

                        Log.d("MainActivity", "onResult: " + result.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "初始化认证成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onError(OCRError error) {
                        error.printStackTrace();
                        Log.e("MainActivity", "onError: " + error.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "初始化认证失败,请检查 key", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }, getApplicationContext(),
                // 需要自己配置 https://console.bce.baidu.com
                "gWAvUEOyEEtIIGaGy20zwl5W",
                // 需要自己配置 https://console.bce.baidu.com
                "fP8fq0rOKTKxQlnCbu5aN8oTFv5CElql");
    }

    private void initLicense() {
        CameraNativeHelper.init(this, OCR.getInstance().getLicense(),
                new CameraNativeHelper.CameraNativeInitCallback() {
                    @Override
                    public void onError(int errorCode, Throwable e) {
                        final String msg;
                        switch (errorCode) {
                            case CameraView.NATIVE_SOLOAD_FAIL:
                                msg = "加载so失败，请确保apk中存在ui部分的so";
                                break;
                            case CameraView.NATIVE_AUTH_FAIL:
                                msg = "授权本地质量控制token获取失败";
                                break;
                            case CameraView.NATIVE_INIT_FAIL:
                                msg = "本地质量控制";
                                break;
                            default:
                                msg = String.valueOf(errorCode);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,
                                        "本地质量控制初始化错误，错误原因： " + msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                String filePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();
                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
                    } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath);
                    } else if (CameraActivity.CONTENT_TYPE_BANK_CARD.equals(contentType)) {
                        recCreditCard(filePath);
                    }
                }
            }
        }
        if (requestCode == REQUEST_CODE_DRIVING_LICENSE && resultCode == Activity.RESULT_OK) {
            String filePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();
            recDrivingCard(filePath);
        }
        if (requestCode == REQUEST_CODE_VEHICLE_LICENSE && resultCode == Activity.RESULT_OK) {
            String filePath = FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath();
            recVehicleCard(filePath);
        }
    }

    /**
     * 解析身份证图片
     *
     * @param idCardSide 身份证正反面
     * @param filePath   图片路径
     */
    private void recIDCard(String idCardSide, String filePath) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(40);
        OCR.getInstance().recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                if (result != null) {

                    String name = "";
                    String sex = "";
                    String nation = "";
                    String num = "";
                    String address = "";
                    String signing = "";
                    String expirationdate = "";

                    //姓名
                    if (result.getName() != null) {
                        name = result.getName().toString();
                    }
                    //性别
                    if (result.getGender() != null) {
                        sex = result.getGender().toString();
                    }
                    //民族
                    if (result.getEthnic() != null) {
                        nation = result.getEthnic().toString();
                    }
                    //身份证号码
                    if (result.getIdNumber() != null) {
                        num = result.getIdNumber().toString();
                    }
                    //地址
                    if (result.getAddress() != null) {
                        address = result.getAddress().toString();
                    }
                    //签发机关
                    if (result.getIssueAuthority() != null) {
                        signing = result.getIssueAuthority().toString();
                    }
                    //有效日期
                    if (result.getExpiryDate() != null && result.getSignDate() != null) {
                        expirationdate = result.getExpiryDate().toString() + "---" + result.getSignDate().toString();
                    }


                    Log.d(TAG, "onResult: " +

                            "0:" + result.getIdCardSide() + "\n" +
                            "1:" + result.getImageStatus() + "\n" +
                            "2:" + result.getRiskType() + "\n" +
                            "3:" + result.getBirthday() + "\n" +
                            "4:" + result.getDirection() + "\n" +
                            "5:" + result.getEthnic() + "\n" +
                            "6:" + result.getExpiryDate() + "\n" +
                            "7:" + result.getGender() + "\n" +
                            "8:" + result.getIdNumber() + "\n" +
                            "9:" + result.getIssueAuthority() + "\n" +
                            "10:" + result.getName() + "\n" +
                            "11:" + result.getSignDate() + "\n" +
                            "12:" + result.getWordsResultNumber() + "\n" +
                            "13:" + result.getAddress() + "\n");

                    content.setText(
                            "姓名: " + name + "\n" +
                                    "性别: " + sex + "\n" +
                                    "民族: " + nation + "\n" +
                                    "身份证号码: " + num + "\n" +
                                    "住址: " + address + "\n" +
                                    "签发机关: " + signing + "\n" +
                                    "有效日期: " + expirationdate + "\n"
                    );
                }
            }

            @Override
            public void onError(OCRError error) {
                Toast.makeText(MainActivity.this, "识别出错,请查看log错误代码", Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", "onError: " + error.getMessage());
            }
        });
    }


    /**
     * 解析银行卡
     *
     * @param filePath 图片路径
     */
    private void recCreditCard(String filePath) {
        // 银行卡识别参数设置
        BankCardParams param = new BankCardParams();
        param.setImageFile(new File(filePath));

        // 调用银行卡识别服务
        OCR.getInstance().recognizeBankCard(param, new OnResultListener<BankCardResult>() {
            @Override
            public void onResult(BankCardResult result) {
                if (result != null) {

                    String type;
                    if (result.getBankCardType() == BankCardResult.BankCardType.Credit) {
                        type = "信用卡";
                    } else if (result.getBankCardType() == BankCardResult.BankCardType.Debit) {
                        type = "借记卡";
                    } else {
                        type = "不能识别";
                    }
                    content.setText("银行卡号: " + (!TextUtils.isEmpty(result.getBankCardNumber()) ? result.getBankCardNumber() : "") + "\n" +
                            "银行名称: " + (!TextUtils.isEmpty(result.getBankName()) ? result.getBankName() : "") + "\n" +
                            "银行类型: " + type + "\n");
                }
            }

            @Override
            public void onError(OCRError error) {
                Toast.makeText(MainActivity.this, "识别出错,请查看log错误代码", Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", "onError: " + error.getMessage());
            }
        });
    }

    /**
     * 解析驾驶证
     *
     * @param filePath 图片路径
     */
    private void recDrivingCard(String filePath) {
        // 驾驶证识别参数设置
        OcrRequestParams param = new OcrRequestParams();

        // 设置image参数
        param.setImageFile(new File(filePath));
        // 设置其他参数
        param.putParam("detect_direction", true);
        // 调用驾驶证识别服务
        OCR.getInstance().recognizeDrivingLicense(param, new OnResultListener<OcrResponseResult>() {
            @Override
            public void onResult(OcrResponseResult result) {
                // 调用成功，返回OcrResponseResult对象
                Log.d("MainActivity", result.getJsonRes());
                content.setText(result.getJsonRes());
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError对象
            }
        });
    }

    /**
     * 解析行驶证
     *
     * @param filePath 图片路径
     */
    private void recVehicleCard(String filePath) {
        OcrRequestParams param = new OcrRequestParams();
        param.setImageFile(new File(filePath));
        OCR.getInstance().recognizeVehicleLicense(param, new OnResultListener<OcrResponseResult>() {
            @Override
            public void onResult(OcrResponseResult result) {
                Log.d("MainActivity", result.getJsonRes());
                content.setText(result.getJsonRes());
            }

            @Override
            public void onError(OCRError error) {
                // 调用失败，返回OCRError对象

            }
        });
    }

    @Override
    protected void onDestroy() {
        CameraNativeHelper.release();
        // 释放内存资源
        OCR.getInstance().release();
        super.onDestroy();

    }

}

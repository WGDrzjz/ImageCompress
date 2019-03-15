# ImageCompress
ImageCompress是基于libjpeg-turbo来进行图片的压缩，它的优点在于相较于libjpeg来说压缩时间大大的缩短了（4.5M的图片压缩完耗时约1.2秒），而相较于Android系统的图片压缩来说ImageCompress在图片处理的过程使用了哈弗曼表，这大大的提高了相同体积下图片的清晰度。

查看使用与效果可以去这里：https://blog.csdn.net/hh7181521/article/details/81014839


特别声明：终于有时间来处理这个了，这次优化了OOM问题，总体上讲提供了两种方式（1、耗时短但OOM几率大；2、耗时长但OOM几率很小）


一：添加依赖
  1、 对于Android Studio的用户，可以选择添加:

    compile 'com.wgd.gdcp.gdcplibrary:ImageCompress:1.0.10'
或
    implementation 'com.wgd.gdcp.gdcplibrary:ImageCompress:1.0.10'
    如果报错的话可以在项目的build.gradle中加入

    allprojects {
    repositories {
        maven { url "https://dl.bintray.com/wangruijun/maven" }
        jcenter() 
    }
}

二：使用图片压缩
    （1）、直接用lib库压缩，不会调试旋转角度、图片分辨率大小等

        强调：这里runOnUiThread 方法可以不用，但要注意的是：虽然jar包中处理了，但不保证一定是在主线程中回调。
  

            // path 为图片地址，savePath是图片要保存的地址
            new GDCompress(MainActivity.this, path, savePath, new GDCompressImageListener() {
                @Override
                public void OnSuccess(String path) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }

                @Override
                public void OnError(int code, String errorMsg) {

                }
            });

     （2）、单个图片压缩，多线程模式
            new GDCompressC(MainActivity.this,
                    new GDConfig().setmPath(tempCompressImgPath), new GDCompressImageListener() {
                  @Override
                  public void OnSuccess(String path) {
                      MainActivity.this.runOnUiThread(new Runnable() {
                          @Override
                          public void run() {

                          }
                      });
                  }

                  @Override
                  public void OnError(int code, String errorMsg) {

                  }
              });

      （3）、单个图片压缩，单线程模式
            String tempCompressImgPath = mSelectedPath.get(0);//
            GDImageBean imageBeana = new GDImageBean();
            imageBeana.setmGDConfig(new GDConfig().setmPath(tempCompressImgPath));
            new GDCompressA(MainActivity.this, imageBeana
                    , new GDCompressImageListenerA() {
            GDCompressImageListener() {
                @Override
                public void OnSuccess(GDImageBean gdImageBean) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }

                @Override
                public void OnError(GDImageBean gdImageBean) {

                }
            });

      （4）、批量压缩，多线程模式，速度快，但OOM几率大
            GDCompressC gdCompressC = new GDCompressC(MainActivity.this, new GDCompressImageListener() {
                @Override
                public void OnSuccess(String path) {
                    Log.i(TAG, "OnSuccess: ===批量=====图片压缩=====btn_img_c2========path==" + path);
                }

                @Override
                public void OnError(int code, String errorMsg) {
                    Log.i(TAG, "OnError: ===批量=====图片压缩=====btn_img_c2==========");
                }
            });
            for (int i = 0; i < mSelected.size(); i++) {
                gdCompressC.start(new GDConfig().setmPath(mSelectedPath.get(i)));
            }

      （5）、批量压缩，单线程模式，速度慢，但OOM几率小很多
            GDCompressA gdCompressA = new GDCompressA(MainActivity.this, new GDCompressImageListenerA() {
                @Override
                public void OnSuccess(GDImageBean gdImageBean) {
                    Log.i(TAG, "OnSuccess: ===批量=====图片压缩=====btn_img_c3========path==" + gdImageBean.getmGDConfig().getmPath());
                }

                @Override
                public void OnError(GDImageBean gdImageBean) {
                    Log.i(TAG, "OnError: ===批量=====图片压缩=====btn_img_c3==========");
                }
            });
            for (int i = 0; i < mSelected.size(); i++) {
                GDImageBean imageBeana = new GDImageBean();
                imageBeana.setmGDConfig(new GDConfig().setmPath(mSelectedPath.get(i)));
                gdCompressA.start(imageBeana);
            }

      （6）、多个图片同时压缩（GDConfig中属性下边统一解释）
            注：这里OnError方法中返回的数据GDImageBean中通过code字段来判断图片是否成功压缩；而OnSuccess方法中所有的图片都是压缩完成的（即只要有一                个图片压缩失败都是回调OnError方法）。
            List<GDImageBean> imageBeans = new ArrayList<>();
            for (int i = 0; i < selectList.size(); i++) {
                final String imgpath = selectList.get(i).getPath();
                imageBeans.add(new GDImageBean(new GDConfig().setmPath(imgpath)));
            }
            new GDCompressImageS(SendCircleActivity.this, imageBeans, new GDCompressImageSListener() {    @Override
                public void OnSuccess(List<GDImageBean> imageBeanList) {

                }

                @Override
                public void OnError(List<GDImageBean> imageBeanList) {

                }
            });

        （7）、GDConfig中属性统一解释
              new GDConfig()
                      .setmPath(tempCompressImgPath)//要压缩图片的原路径
                      .setSavePath(tempCompressImgPath)//压缩图片的保存路径，如果不设置将替换原文件
                      .setChangeWH(true)//是否要进行调整图片分辨率以压缩到更小
                      .setWidth(720)//需要调整分辨率的时候有效，压缩后的宽度（按比例计算后的，而不是直接使用这个）
                      .setHeight(1280)//需要调整分辨率的时候有效，压缩后的高度（按比例计算后的，而不是直接使用这个）

        
联系作者：QQ：1772889689     邮箱：1772889689@qq.com    微信：gdihh8180
注：联系请注明联系目的

# ImageCompress
ImageCompress是基于libjpeg-turbo来进行图片的压缩，它的优点在于相较于libjpeg来说压缩时间大大的缩短了（4.5M的图片压缩完耗时约1.2秒），而相较于Android系统的图片压缩来说ImageCompress在图片处理的过程使用了哈弗曼表，这大大的提高了相同体积下图片的清晰度。

查看使用与效果可以去这里：https://blog.csdn.net/hh7181521/article/details/81014839

一：添加依赖
  1、 对于Android Studio的用户，可以选择添加:


    compile 'com.wgd.gdcp.gdcplibrary:ImageCompress:1.0.9'
或
implementation 'com.wgd.gdcp.gdcplibrary:ImageCompress:1.0.9'
    如果报错的话可以在项目的build.gradle中加入

    allprojects {
    repositories {
        maven { url "https://dl.bintray.com/wangruijun/maven" }
    }

}

或

allprojects {
    repositories {
        jcenter() 
    }

}
二：使用图片压缩
  （1）、普通压缩方式（前期方式，后期版本也会一直兼容）

        强调：这里runOnUiThread 方法可以不用，但要注意的是：虽然jar包中处理了，但不保证一定是在主线程中回调。


new GDCompress(MainActivity.this, tempCompressImgPath, tempCompressImgPath, new GDCompressImageListener() {
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

    （2）、多个图片同时压缩（GDConfig中属性下边统一解释）

            注：这里OnError方法中返回的数据GDImageBean中通过code字段来判断图片是否成功压缩；而OnSuccess方法中所有的图片都是压缩完成的（即只要有一个图片压缩失败都是回调OnError方法）。


List<GDImageBean> imageBeans = new ArrayList<>();
for (int i = 0; i < selectList.size(); i++) {
    final String imgpath = selectList.get(i).getPath();
    imageBeans.add(new GDImageBean(new GDConfig().setmPath(imgpath)));
}
new GDCompressImageS(SendCircleActivity.this, new GDCompressImageSListener() {
    @Override
    public void OnSuccess(List<GDImageBean> imageBeanList) {
        
    }

    @Override
    public void OnError(List<GDImageBean> imageBeanList) {
        
    }
}, imageBeans);
        （3）、可控制图片分辨率大小的压缩（GDConfig中属性下边统一解释）




new GDCompressC(MainActivity.this, new GDConfig().setmPath(tempCompressImgPath).setChangeWH(true), new GDCompressImageListener() {
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

        （4）、GDConfig中属性统一解释

            

new GDConfig()
        .setmPath(tempCompressImgPath)//要压缩图片的原路径
        .setSavePath(tempCompressImgPath)//压缩图片的保存路径，如果不设置将替换原文件
        .setChangeWH(true)//是否要进行调整图片分辨率以压缩到更小
        .setWidth(720)//需要调整分辨率的时候有效，压缩后的宽度（按比例计算后的，而不是直接使用这个）
        .setHeight(1280)//需要调整分辨率的时候有效，压缩后的高度（按比例计算后的，而不是直接使用这个）
        
联系作者：QQ：1772889689     邮箱：1772889689@qq.com    微信：gdihh8180
注：联系请注明联系目的

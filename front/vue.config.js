
const webpack = require('webpack');

module.exports = {
    publicPath: './',
    assetsDir: "static",
    // 跨域配置
    devServer: {                //记住，别写错了devServer//设置本地默认端口  选填
        port: 9876,
        proxy: {                 //设置代理，必须填
            [process.env.VUE_APP_BASE_API]: {              //设置拦截器  拦截器格式   斜杠+拦截器名字，名字可以自己定
                target: 'http://localhost:8083/',     //代理的目标地址
                changeOrigin: true,              //是否设置同源，输入是的
                pathRewrite: {                   //路径重写
                    ['^' + process.env.VUE_APP_BASE_API]: ''                     //选择忽略拦截器里面的单词
                }
            }
        }
    },
    configureWebpack: {//引入jquery
        plugins: [
            new webpack.ProvidePlugin({
                $:"jquery",
                jQuery:"jquery",
                "windows.jQuery":"jquery"
            }),
        ]
    },

}
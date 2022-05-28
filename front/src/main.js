/*
 * @Author: your name
 * @Date: 2021-12-27 13:56:51
 * @LastEditTime: 2022-05-22 11:27:10
 * @LastEditors: BinjieYuan yuanbj9035@163.com
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \flaskVueSEIMS\client\src\main.js
 */
import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store/index'
import ViewUI from 'view-design'
import 'view-design/dist/styles/iview.css'
import vuescroll from 'vuescroll';
import $ from 'jquery'
import 'leaflet/dist/leaflet.css'
import  L from 'leaflet'
import 'leaflet.pm'
import 'leaflet.pm/dist/leaflet.pm.css'
import '@supermap/iclient-leaflet';


// Vue.config.productionTip = process.env.NODE_ENV === 'development';  //production生产环境  development开发环境
Vue.use(vuescroll); // install the vuescroll first
Vue.prototype.$vuescrollConfig = {
  bar: {
    background: '#808695'
  }
};
Vue.prototype.$ = $
Vue.config.productionTip = false
// Vue.prototype.$L=L
Vue.use(ViewUI)
Vue.use(L)
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

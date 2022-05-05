/*
 * @Author: your name
 * @Date: 2021-12-27 13:56:51
 * @LastEditTime: 2022-04-28 09:53:34
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \flaskVueSEIMS\client\src\router\index.js
 */
import Vue from 'vue'
import VueRouter from 'vue-router'

import Home from '../components/Home.vue'
import BaseMap from '../components/MapView.vue'
import BasinShow from '../components/mapcontrol/BasinShow.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name:'Home',
    redirect: '/home',
    component:Home,
    // children:[
    //   {
    //     path: '/home',
    //     name:'',
    //     component: BaseMap
    //   },
    //   {
    //     path: '/map/BasinShow',
    //     name:'',
    //     component: BasinShow
    //   },
    // ]
  },
]

const router = new VueRouter({
  // mode: 'history',
  base: process.env.BASE_URL,
  routes
})
const VueRouterPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (to) {
  return VueRouterPush.call(this, to).catch(err => err)
}

export default router

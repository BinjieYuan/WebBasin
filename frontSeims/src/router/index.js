/*
 * @Author: your name
 * @Date: 2021-12-27 13:56:51
 * @LastEditTime: 2022-04-13 18:02:11
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \flaskVueSEIMS\client\src\router\index.js
 */
import Vue from 'vue'
import VueRouter from 'vue-router'

const BaseLayer = () => import('../views/BaseLayer.vue')
const BasinShow = () => import('../views/BasinShow.vue')


Vue.use(VueRouter)

const routes = [
  {
    path: '',
    redirect: '/home'
  },
  {
    path: '/home',
    component: BaseLayer
  },
  {
    path: '/basinshow',
    component: BasinShow
  },
]

const router = new VueRouter({
  // mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router

/*
 * @Author: your name
 * @Date: 2021-12-27 14:25:53
 * @LastEditTime: 2022-04-17 22:57:00
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \flaskVueSEIMS\client\src\network\request.js
 */
import axios from 'axios'

// export function request(config, success, failure) {
    //1.创建axios的实例
const request = axios.create({
        baseURL: process.env.VUE_APP_BASE_API,
        timeout: 100000,

    })
    // // 2.axios的拦截器
    // instance.interceptors.request.use(config => {
    //     console.log(config);
    //     return config
    // },err => {
    //     console.log(err);
    // })
    //3.发送真正的网络请求
//     return instance(config)

// }
export default request
<!--
 * @Author: your name
 * @Date: 2021-12-27 13:56:51
 * @LastEditTime: 2022-04-28 09:58:01
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \flaskVueSEIMS\client\src\App.vue
-->
<template>
  <div id="app">
      <!-- <base-layer></base-layer> -->
      <Home></Home>
      <!-- <router-link to="/home"></router-link> 
      <router-link to="/basinShow"></router-link> -->
      <!-- <router-view></router-view> -->
  </div>
</template>

<script>
  // import BaseLayer from './views/BaseLayer.vue'
  // import BasinShow from './views/BasinShow.vue'
  import Home from './components/Home.vue'

export default {
  name: 'App',
  components: {
    Home
  }
}
</script>
<style scoped>

</style>

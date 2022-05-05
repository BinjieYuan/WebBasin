<!--
 * @Author: your name
 * @Date: 2021-12-27 13:56:51
 * @LastEditTime: 2021-12-28 16:21:48
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \flaskVueSEIMS\client\src\App.vue
-->
<template>
  <div id="app">
      <!-- <base-layer></base-layer> -->
      <main-layer></main-layer>
      <!-- <router-link to="/home"></router-link> 
      <router-link to="/basinShow"></router-link> -->
      <!-- <router-view></router-view> -->
  </div>
</template>

<script>
  // import BaseLayer from './views/BaseLayer.vue'
  // import BasinShow from './views/BasinShow.vue'
  import MainLayer from './components/MainLayer.vue'

export default {
  name: 'App',
  components: {
    MainLayer
  }
}
</script>
<style>

</style>

import Vue from 'vue'
import Vuex from 'vuex'

import MapView from '../store/modules/MapViewState'
import GeoDataControl from '../store/modules/GeoDataControl'
// import createPersistedState from "vuex-persistedstate"
Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    MapView,
    GeoDataControl,
  },
  // state: {
  // },
  // mutations: {
  // },
  // actions: {
  // },
  // modules: {
  // }
  // plugins: [createPersistedState({
  //   storage: window.sessionStorage,
  //   reducer(val) {
  //     return { // 只储存state中的
  //       MapView: val.storeMap,
        

  //     }
  //   }
  // })]
})

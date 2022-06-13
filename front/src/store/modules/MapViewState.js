import L from 'leaflet';
import createPersistedState from "vuex-persistedstate"

const state = () =>  ({
  storeMap: null,
  levelSelect:7,
  mapLayers: {
    basinLayer: null,
  }
})

const getters = {
  storeMap: state => state.storeMap
}

const mutations = {
  SET_MAP_OBJECT(state, map) {
    state.storeMap = map;
  },

}
const actions = {


}



export default {
    state,
    getters,
    actions,
    mutations,
//     plugins: [createPersistedState({
//         storage: window.sessionStorage,
//         reducer(val) {
//           return { // 只储存state中的
//             storeMap: val.storeMap,
            
//           }
//         }
//       })]
}
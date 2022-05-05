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
  SET_MAP_ADDLAYER(state, addLayer) {
    state.storeMap.addLayer(addLayer);
  },
  SET_BASIN_LAYER(state, layer) {
    state.mapLayers.basinLayer = layer;
  },
  SET_BASIN_LEVEL(state, level){
    state.levelSelect = level
  },
}
const actions = {
  changeStandardBasinsLevel({commit}, basinLevel){
      try{
          state.mapLayers.basinLayer.remove();
      }catch{}
      var level = basinLevel;
    //   state.mapLayers.basinLayer = L.tileLayer.wms('http://210.26.48.56:30122/geoserver/Basin_shp/wms',{
      

      var basinLayerWms = L.tileLayer.wms('http://localhost:8081/geoserver/Basin_shp/wms',{
          layers: 'Asia_level_0' + level,
          format: 'image/png',
          transparent: true,
          noWarp:true
      });
    //   .addTo(state.storeMap);

      var basinLayerShow = new L.LayerGroup().addLayer(basinLayerWms);
    //   basinLayerShow.addTo(state.storeMap);
    //   console.log(basinLayerWms);

      commit('SET_BASIN_LAYER',basinLayerShow)
      commit('SET_BASIN_LEVEL',basinLevel)
      console.log('Show Standard Basin Layer in Level ' + level + '.');
  },


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
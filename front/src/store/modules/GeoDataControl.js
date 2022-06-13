
const state = () =>  ({
  projectName: null,
})

const getters = {
  projectName: state => state.projectName
}

const mutations = {
  SET_PROJECT_NAME(state, name) {
    state.projectName = name;
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
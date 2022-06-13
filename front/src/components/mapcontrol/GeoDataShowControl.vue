<!--
 * @Author: BinjieYuan yuanbj9035@163.com
 * @Date: 2022-05-08 21:49:16
 * @LastEditors: BinjieYuan yuanbj9035@163.com
 * @LastEditTime: 2022-05-31 08:50:42
 * @FilePath: \WebBasin\front\src\components\mapcontrol\GeoDataShowControl.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
    <!-- optionally define the sidebar content via HTML markup -->
    <div id="sidebar" class="leaflet-sidebar collapsed" style="height: 80%;">
      <!-- nav tabs -->
      <div class="leaflet-sidebar-tabs">
        <!-- top aligned tabs -->
        <ul role="tablist">
          <li><a href="#home" role="tab"><i class="fa fa-bars active"></i></a></li>
          <li><a href="#map" role="tab"><Icon type="md-map" /></a></li>
          <li><a href="#autopan" role="tab"><i class="fa fa-arrows"></i></a></li>
        </ul>
        <!-- bottom aligned tabs -->
        <ul role="tablist">
          <li><a href="https://github.com/nickpeihl/leaflet-sidebar-v2"><i class="fa fa-github"></i></a></li>
        </ul>
      </div>
      <!-- panel content -->
      <div class="leaflet-sidebar-content">
          <div class="leaflet-sidebar-pane" id="home">
            <h1 class="leaflet-sidebar-header">
              sidebar-v2
              <span class="leaflet-sidebar-close"><i class="fa fa-caret-left"></i></span>
            </h1>
          </div>
          <div class="leaflet-sidebar-pane" id="map">
            <h1 class="leaflet-sidebar-header">
              空间数据查看
              <span class="leaflet-sidebar-close"><i class="fa fa-caret-left"></i></span>
            </h1>
            <div id="BackgroundColor">
              <Collapse id="CollapseBackgroundColor" v-model="PanelShowValue">
                <Panel name="vector">
                  矢量数据
                  <div slot="content">
                    <CheckboxGroup v-model="vectorGroup">
                      <Checkbox label="vectorBasinScope" >流域边界</Checkbox><br>
                      <Checkbox label="vectorOutlet">流域出口</Checkbox><br>
                      <Checkbox label="vectorStream">河网</Checkbox><br>
                      <Checkbox label="vectorSubbasin">子流域</Checkbox><br>
                    </CheckboxGroup>
                  </div>
                </Panel>
                <Panel name="raster">
                  栅格数据
                  <div slot="content">
                    <CheckboxGroup v-model="rasterGroup" @on-change="controlRasterLayer">
                      <Checkbox label="dem" >DEM高程</Checkbox><br>
                      <Checkbox label="rasterBaisn">流域范围</Checkbox><br>
                      <Checkbox label="rasterFlowDir">流向</Checkbox><br>
                      <Checkbox label="acc">汇流累积量</Checkbox><br>
                      <Checkbox label="rasterSubbasin">子流域栅格</Checkbox><br>
                      <Checkbox label="rasterStreamLink">Stream Link</Checkbox><br>
                      <Checkbox label="rasterLanduse">土地利用</Checkbox><br>
                      <Checkbox label="rasterSoiltype">土壤属性</Checkbox><br>
                    </CheckboxGroup>
                  </div>
                </Panel>
              </Collapse>
            </div>
          </div>
          <div class="leaflet-sidebar-pane" id="autopan">
            <h1 class="leaflet-sidebar-header">
              autopan
              <span class="leaflet-sidebar-close"><i class="fa fa-caret-left"></i></span>
            </h1>
            <p>
              <code>Leaflet.control.sidebar({ autopan: true })</code>
              makes shure that the map center always stays visible.
            </p>
            <p>
              The autopan behviour is responsive as well.
              Try opening and closing the sidebar from this pane!
            </p>
          </div>
      </div>
    </div>

</template>

<script>
import "@/assets/js/leaflet-sidebar.min.js";
export default {
  name: 'GeoDataShowControl',
  data(){
      return {
        map:null,
        basinDataLayerGroup: null,
        PanelShowValue:['vector', 'raster'],
        vectorGroup:[],
        rasterGroup:[],
        vectorShowGroup:[],
        rasterShowGroup:[],
        vectorLayerId:'',
        rasterLayerId:'',
        projectname:'',
      }
    },

  mounted(){
    this.map=this.$store.getters.storeMap;
    this.addSideBar();
    this.initBasinLayerGroup();
    this.vectorLayerId=new Map();
    this.rasterLayerId=new Map();
  },
  updated() {
    this.projectName=this.$store.getters.projectName;
  },
  methods: {
    addSideBar(){
      var sidebar = L.control.sidebar({ container: 'sidebar' })
      .addTo(this.map);
      // .open('home');
      sidebar.on('content', function (ev) {
        switch (ev.id) {
          case 'autopan':
          sidebar.options.autopan = true;
          break;
          default:
          sidebar.options.autopan = false;
        }
      });
    },
    initBasinLayerGroup(){
      this.basinDataLayerGroup = L.layerGroup([]);
      this.basinDataLayerGroup.addTo(this.map);
    },
    controlRasterLayer(arr){
      if(this.rasterGroup.length>this.rasterShowGroup.length){
        console.log("执行if");
        // let b = new Set(this.rasterShowGroup);
        // let addLayer = [...arr].filter(x => !b.has(x));  
        var addLayer = this.diff(this.rasterGroup,this.rasterShowGroup);
        console.log(arr);
        console.log(this.rasterShowGroup);
        console.log(addLayer);
        this.showBasinDataLayer(addLayer[0]);
        this.rasterShowGroup.push(addLayer[0]);
      }
      else{
          console.log("执行else");
    //   else if (arr.length<this.rasterShowGroup.length){
        console.log(arr);
        console.log(this.rasterShowGroup);
        var removeLayer = this.getArrDifference(this.rasterShowGroup,this.rasterGroup);
        console.log(removeLayer[0]);
        var removeId = this.rasterLayerId.get(removeLayer[0]);
        console.log(removeId);
        this.basinDataLayerGroup.removeLayer(removeId);
        this.rasterShowGroup.splice(this.rasterShowGroup.indexOf(removeLayer), 1);
        this.rasterLayerId.delete(removeLayer[0]);
      }
    },
    showBasinDataLayer(layerName){
        console.log("执行showBasinDataLayer");
      console.log(layerName);
      var baseUrl='http://223.2.40.37:7070/geoserver/';
      var wmsURL=baseUrl + 'test' + '/wms';
      //   var wmsURL=baseUrl + this.projectName + '/wms';
      //   this.basinLayer = L.tileLayer.wms('http://210.26.48.56:30122/geoserver/Basin_shp/wms',{
      
      var layer = new L.tileLayer.wms(wmsURL,{
          layers: layerName,
          format: 'image/png',
          transparent: true,
          noWarp:true
      }).addTo(this.basinDataLayerGroup);
      var layerId=this.basinDataLayerGroup.getLayerId(layer);
      this.rasterLayerId.set(layerName,layerId);
    },
    getArrDifference(arr1, arr2) {
      let b = new Set(arr2);
      let difference = [...arr1].filter(x => !b.has(x));
      return difference;
    },
    diff(arr1, arr2) {
        var newArr = [];
        var arr3=arr1.concat(arr2);//将arr1和arr2合并为arr3
        function isContain(value){
        //找出arr3中不存在于arr1和arr2中的元素
        return arr1.indexOf(value)==-1||arr2.indexOf(value)==-1;
        }
        newArr = arr3.filter(isContain);
        return newArr;
    },


  }
}
</script>

<style>
  @import url('../../assets/css/leaflet-sidebar.min.css');
  @import url('https://maxcdn.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css');
</style>
<style>
    #BackgroundColor .ivu-collapse{
        background-color: rgba(247,247,247, 0.01);
    }
    #CollapseBackgroundColor .ivu-collapse-header{
        background-color: rgba(247,247,247, 0.7);
    }
    #CollapseBackgroundColor .ivu-collapse-content{
        background-color: rgba(255,255,255, 0.7);
    }
</style>
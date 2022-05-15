<!--
 * @Author: BinjieYuan yuanbj9035@163.com
 * @Date: 2022-05-08 21:49:16
 * @LastEditors: BinjieYuan yuanbj9035@163.com
 * @LastEditTime: 2022-05-13 20:36:51
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
            <div>
              <Collapse v-model="PanelShowValue">
                <Panel name="vector">
                  矢量数据
                  <div slot="content">
                    <CheckboxGroup v-model="vectorGroup">
                      <Checkbox label="vectorBasinScope">流域边界</Checkbox><br>
                      <Checkbox label="vectorOutlet">流域出口</Checkbox><br>
                      <Checkbox label="vectorStream">河网</Checkbox><br>
                      <Checkbox label="vectorSubbasin">子流域</Checkbox><br>
                    </CheckboxGroup>
                  </div>
                </Panel>
                <Panel name="raster">
                  栅格数据
                  <div slot="content">
                    <CheckboxGroup v-model="rasterGroup">
                      <Checkbox label="rasterDEM">DEM高程</Checkbox><br>
                      <Checkbox label="rasterBaisn">流域范围</Checkbox><br>
                      <Checkbox label="rasterSubbasin">子流域栅格</Checkbox><br>
                      <Checkbox label="rasterStreamLink">Stream Link</Checkbox><br>
                      <Checkbox label="rasterLanduse">土地利用</Checkbox><br>
                      <Checkbox label="rasterSoiltype">土壤属性</Checkbox><br>
                      <Checkbox label="rasterFlowDir">流向</Checkbox><br>
                      <Checkbox label="rasterAcc">汇流累积量</Checkbox><br>
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
        PanelShowValue:['vector', 'raster'],
        vectorGroup:[],
        rasterGroup:[],
        
      }
    },

  mounted(){
    this.map=this.$store.getters.storeMap;
    this.addSideBar();
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

  },
}
</script>

<style>
  @import url('../../assets/css/leaflet-sidebar.min.css');
  @import url('https://maxcdn.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css');
</style>
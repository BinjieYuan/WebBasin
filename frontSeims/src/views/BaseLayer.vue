<!--
 * @Author: your name
 * @Date: 2021-12-27 13:56:51
 * @LastEditTime: 2022-04-13 16:42:33
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \flaskVueSEIMS\client\src\views\Home.vue
-->
<template>
  <div class="home">
    <div class="map" id="leafletMap"></div>
  </div>
</template>

<script>
// @ is an alias to /src

export default {
  name: 'BaseLayer',
    mounted(){
      this.initMap();
      this.initController();
      this.initLayers();
      this.listenDraw();
  },
  data(){
      return {
          mapEl: null,
          baseLayers: null,
          drawingLayerGroup: null,
          riverLayer: null,
          riverCkb: false,
          basinCkb:true,
          basinType: 'standard',
          levelSelect: 7,
          levelSelectCustom: 5,
          levelLayerList:[],
          basinLayer: null,
          wholeUpstreamLayer:null,
          lakesLayer:null,
          wholeBasinLayer: null,
          wholeBasinCkb:false,
          wholeUpstreamCkb:false,
          uploadCSVModal:false,
          toUploadCSV: null,
          csvName: "",
          batchStatus:"csvUpload",
          basinRangesUrl:"",
          uploadGeoJSONModal:false,
          lisfloodModal: false,
          toUploadGeoJSON: null,
          geoJSONName:"",
          downloadDataModal:false,
          levelSelectDownload: 1,
          loadingDEM:false,
          loadingDir:false,
          loadingAcc:false,
          loadingLake:false,
          loadingBasin:false,
          loadingSlope:false,
          loadingRiver:false,
          resetDataModal:false,
          customDataModal:false,
          showDownloadGeoJSONBtn: false,
          // csvQueryLevel: 5,
          toDownloadGeoJSONStr: "",
          querySwitch:false,
          queryType:"standard",
          levelSelectQuery:5,
          paramData:{
              rainParam: "",
              startParamD: "",
              startParamT: "",
              endParamD: "",
              endParamT: ""
          },
          resetData:{
              riverThreshold: 100.0,
              lakeThreshold: 1000.0
          },
          resetValidate:{
              riverThreshold:[
                  { required: true, message: 'The value cannot be empty', trigger: 'blur' }
              ],
              lakeThreshold:[
                  { required: true, message: 'The value cannot be empty', trigger: 'blur' }
              ]
          },
          resetStatus: 'resetSetting',
          resultDataUrl: '',
          customDataSetting:{
              demFileName:'',
              lakeZipName:'',
              riverThreshold: 100.0,
              // lakeThreshold: 1000.0
          },
          customValidate:{
              demFileName:[
                  { required: true, message: 'The file is required', trigger: 'blur' }
              ],
              lakeZipName:[
                  { required: true, message: 'The file is required', trigger: 'blur' }
              ],
              riverThreshold:[
                  { required: true, message: 'The value cannot be empty', trigger: 'blur' }
              ],
              lakeThreshold:[
                  { required: true, message: 'The value cannot be empty', trigger: 'blur' }
              ]
          },
          customStatus: 'customSetting',
          customDataUrl: '',
          scrollOps: {
              bar: {
                  background: "#808695"
              }
          },
      }
  },
  methods:{
      initMap(){
          this.mapEl = L.map('leafletMap',{
              zoom: 5,
              center: [34, 110],
              crs: L.CRS.TianDiTu_WGS84,
              timeDimension: true,
              timeDimensionControl: true,
          });
      },
      initController(){
          this.drawingLayerGroup = L.layerGroup([]);
          this.drawingLayerGroup.addTo(this.mapEl);
      },
      initLayers(){
          // 图层控件
          // var mapboxVectorMap = 'https://api.mapbox.com/styles/v1/mapbox/streets-v11/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoiYm9zaDAxMTMiLCJhIjoiY2tsM3FicnprMTMyZTJvbzRpeXF4Y2ZoOSJ9.JFmrSXBF0bTcqyXXnWjLYQ';
          // var vectorMap = L.tileLayer(mapboxVectorMap, { maxZoom: 18 });
          // var vector = L.layerGroup([vectorMap]);
          
          // var osmVectorMap = 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
          // var osmVectorMap = L.tileLayer(osmVectorMap, { maxZoom: 18 });
          // var vectorOSM = L.layerGroup([osmVectorMap]);
          
          // var tdtImgMap =
          //     "http://t0.tianditu.gov.cn/img_w/wmts?tk=d6b0b78f412853967d91042483385d2c" +
          //     "&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=img&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}";
          // var tdtImgAno =
          //     "http://t0.tianditu.gov.cn/eia_w/wmts?tk=d6b0b78f412853967d91042483385d2c" +
          //     "&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=eia&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}";
          // var satelliteMap = L.tileLayer(tdtImgMap, {
          //     maxZoom: 18,
          //     attribution:
          //         '&copy; <a href="http://map.tianditu.gov.cn/">tianditu</a> contributors'
          // });
          // var satelliteAno = L.tileLayer(tdtImgAno, { maxZoom: 18 });
          // var satellite = L.layerGroup([satelliteMap, satelliteAno]);
          // var tdtTerrMap =
          //     "http://t0.tianditu.com/ter_w/wmts?tk=d6b0b78f412853967d91042483385d2c" +
          //     "&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=ter&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}";
          // var tdtTerrAno =
          //     "http://t0.tianditu.com/eta_w/wmts?tk=d6b0b78f412853967d91042483385d2c" +
          //     "&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=eta&STYLE=default&TILEMATRIXSET=w&FORMAT=tiles&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}";
          // var terrainMap = L.tileLayer(tdtTerrMap, {
          //     maxZoom: 18,
          //     attribution:
          //         '&copy; <a href="http://map.tianditu.gov.cn/">tianditu</a> contributors'
          // });
          // var terrainAno = L.tileLayer(tdtTerrAno, { maxZoom: 18 });
          // var terrain = L.layerGroup([terrainMap, terrainAno]);
          // this.baseLayers = {
          //     "Mapbox Vector map": vector,
          //     "OSM Vector map": vectorOSM,
          //     "TDT Satellite map": satellite,
          //     "TDT Terrain map": terrain
          // };
          // var overlayLayers = {};
          // L.control.layers(this.baseLayers, overlayLayers).addTo(this.mapEl);
          // this.baseLayers["Mapbox Vector map"].addTo(this.mapEl);
          
          // // 底图均默认为0层级
          // this.baseLayers["Mapbox Vector map"].setZIndex(0);
          // this.baseLayers["OSM Vector map"].setZIndex(0);
          // this.baseLayers["TDT Satellite map"].setZIndex(0);
          // this.baseLayers["TDT Terrain map"].setZIndex(0);
          var normalMap = L.supermap.tiandituTileLayer
          ({
              key:"56b0c218a6c4020cd38b5fc2be7c6202",
              layerType: "vec"
          });
          var normalLable = L.supermap.tiandituTileLayer
          ({
              key:"56b0c218a6c4020cd38b5fc2be7c6202",
              layerType: "vec",
              isLabel: true
          })
          var imgMap = L.supermap.tiandituTileLayer
          ({
              key:"56b0c218a6c4020cd38b5fc2be7c6202",
              layerType: "img"
          });
          var imgLable = L.supermap.tiandituTileLayer
          ({
              key:"56b0c218a6c4020cd38b5fc2be7c6202",
              layerType: "img",
              isLabel: true
          })
          var terrainMap = L.supermap.tiandituTileLayer
          ({
              key:"56b0c218a6c4020cd38b5fc2be7c6202",
              layerType: "ter"
          });
          var terrainLable = L.supermap.tiandituTileLayer
          ({
              key:"56b0c218a6c4020cd38b5fc2be7c6202",
              layerType: "ter",
              isLabel: true
          })
          //图层与注记合并
          var normal = L.layerGroup([normalMap, normalLable]);
          var img = L.layerGroup([imgMap, imgLable]);
          var terrain = L.layerGroup([terrainMap, terrainLable]);
          this.baseLayers = {
              "矢量地图": normal,
              "影像地图": img,
              "地形地图": terrain
          };
          var overlayLayers = {};
          L.control.layers(this.baseLayers, overlayLayers).addTo(this.mapEl);
          this.baseLayers["矢量地图"].addTo(this.mapEl);
          
          // 底图均默认为0层级
          this.baseLayers["矢量地图"].setZIndex(0);
          this.baseLayers["影像地图"].setZIndex(0);
          this.baseLayers["地形地图"].setZIndex(0);
          // 比例尺
          L.control
              .scale({
                  position: "bottomleft"
              })
              .addTo(this.mapEl);
          // 绘图控件
          var options = {
              position: "topleft", // toolbar position, options are 'topleft', 'topright', 'bottomleft', 'bottomright'
              drawMarker: false, // adds button to draw markers
              drawPolyline: false, // adds button to draw a polyline
              drawRectangle: true, // adds button to draw a rectangle
              drawPolygon: true, // adds button to draw a polygon
              drawCircle: false, // adds button to draw a cricle
              cutPolygon: false, // adds button to cut a hole in a polygon
              editMode: true, // adds button to toggle edit mode for all layers
              dragMode: false,
              removalMode: true // adds a button to remove layers
          };
      },
      listenDraw(){
          var _this = this;
          this.mapEl.on('pm:create', e=>{
              _this.showDownloadGeoJSONBtn = false;
              _this.drawingLayerGroup.clearLayers();
              _this.drawingLayerGroup.addLayer(e.layer);
          });
          this.mapEl.on('pm:remove', e=>{
              _this.showDownloadGeoJSONBtn = false;
              _this.drawingLayerGroup.clearLayers();
          });
          this.mapEl.on('click', e=>{
              if(_this.querySwitch){
                  var latlng = e.latlng
                  var lon = latlng['lng'];
                  var lat = latlng['lat'];
                  var baseUrl="";
                  switch(_this.queryType){
                      case "standard":{
                          //各级流域范围接口待补充
                          // baseUrl = "/basins/querySubLevel/" + this.levelSelectQuery;
                          baseUrl = "/basins/querySubLevel/" + this.levelSelect;
                          break;
                      }
                      case "upstream":{
                          //水体上游范围接口待补充
                          baseUrl = "/basins/upstream";
                          break;
                      }
                      case "whole":{
                          //临时写法
                          baseUrl = "/basins/queryScope"
                      }
                  }
                  this.$Spin.show();
                  axios
                  .get(baseUrl + "?lon=" + lon +"&lat=" + lat)
                  .then(res=>{
                      if(res.data!=0){
                          var tempGeoJSON = res.data;
                          _this.showDownloadGeoJSONBtn = true;
                          _this.toDownloadGeoJSONStr = tempGeoJSON;
                          _this.addGeoJSONToMap(JSON.stringify(tempGeoJSON), "red");
                          _this.$Spin.hide();
                      }
                      else{
                          _this.$Message.error('No Basin Info Here.');
                          _this.$Spin.hide();
                      };
                  })
                  .catch(err=>{
                      confirm('Something Wrong!');
                      _this.$Spin.hide();
                  });
                  // this.showDownloadGeoJSONBtn = true;
                  // this.toDownloadGeoJSONStr = basin_scale;
                  // this.addGeoJSONToMap(JSON.stringify(basin_scale), "red");
                  
              }
          });
      },
  }
}
</script>

<style>
  @import url('../assets/css/base.css');
</style>
<!--
 * @Author: your name
 * @Date: 2022-04-28 13:53:55
 * @LastEditTime: 2022-06-13 17:11:07
 * @LastEditors: BinjieYuan yuanbj9035@163.com
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \WebBasin\front\src\components\seimscontrol\CreateModel.vue
-->
<template>
  <Modal v-model="createModelShowTemp" draggable scrollable :mask="false" title="SEIMS 建模" width="550px">
    <div>
      <Tabs type="card" :animated="false" >
        <!--0、工程项目-->
        <TabPane label="工程管理">
          <div>
            <Divider orientation="left">新建工程</Divider>
            <div class="selectFlex">
              <Form :model="formProjectItem" :label-width="80" :rules="ruleProject" ref="formProjectItem" inline>
                <FormItem label="项目名称" prop='projectName'>
                  <Input v-model="formProjectItem.projectName" placeholder="请输入项目名称" clearable style="width: 220px"></Input>
                  <Button @click="namedProject('formProjectItem')">确定</Button>
                </FormItem>
                <!-- <FormItem>
                  <Button @click="handleSubmitProject('formProjectItem')">确定</Button>
                </FormItem> -->
              </Form>
            </div>
            <div class="finishBtn">
              <Button type="info" ghost>完成新建项目</Button>
            </div>
          </div>
        </TabPane>   
        <!--1、流域划分-->
        <TabPane label="流域划分">
          <div>
              <Divider orientation="left">建模流域范围</Divider>
              <!-- <h3>建模流域范围</h3> -->
              <div>
                <RadioGroup v-model="basinScopeType" style="margin:5px 0px 5px 28px" @on-change="changeBasinScopeDataResource">
                  <Radio label="basinScopeFromSystem">
                    <span>在系统划分的流域图层中选择</span>
                  </Radio>
                  <Radio label="basinScopeFromLocal" style="padding-left:70px">
                    <span>使用本地上传的DEM数据</span>
                  </Radio>
                </RadioGroup><br>
              </div>
              <div class="selectFlex">
                <div>
                  <div style="margin:4px 0">
                    <Checkbox v-model="basinCkb" @on-change="showBasinsLayer" 
                      :disabled="basinScopeType!=='basinScopeFromSystem'">
                        显示流域图层
                    </Checkbox>
                    <Select v-model="levelSelect" size="small" style="width:90px"
                      :disabled="!basinCkb"
                      @on-change="changeStandardBasinsLevel">
                        <Option v-for="item in levelLayerList" :value="item.value" :key="item.value">{{ item.label }}</Option>
                    </Select>
                  </div>
                  <span>开启流域选择</span>
                  <i-switch v-model="querySwitch" :disabled="!basinCkb" @change="querySwitchChange">
                    <Icon slot="open" type="md-pin" size="14"></Icon>
                    <Icon slot="close" type="md-close" size="14"></Icon>
                  </i-switch><br>
                  <div v-if="basinScopeSelectSystem">
                    <Icon type="ios-checkmark-circle-outline" color="#19be6b" size="20"/>
                    <span>已选中流域范围</span>
                    <ButtonGroup size="small">
                      <Button type="success" ghost @click="basinScopeSelectSystemApply" :loading="basinScopeSelectLoadingStatus">确定</Button>
                      <Button type="error" ghost @click="basinScopeSelectSystemReselect">重选</Button>
                    </ButtonGroup>
                  </div>
                  
                </div>
                <div>
                  <Upload action="" class="upload" >
                    <Button icon="ios-cloud-upload-outline" :disabled="basinScopeType=='basinScopeFromSystem'">上传DEM数据</Button>
                  </Upload>
                </div>
              </div>

              <Divider orientation="left">生成河网</Divider>
              <div style="margin:0 34px">
                <Form id="streamValueForm" :model="formStreamNetworkValue" :label-width="0" :rules="ruleStreamNetwork" ref="formStreamNetworkValue" inline>  
                  <span>输入生成河网阈值：</span><br>
                  <FormItem  prop='streamValueCell'>
                    <!-- <InputNumber :min="0" v-model="formStreamNetworkValue.streamValueCell" style="width: 50px"></InputNumber> -->
                    <Input :min="0" v-model="formStreamNetworkValue.streamValueCell" style="width: 90px" @on-change="convertCellArea('cell')"></Input>
                    <!-- <Input :min="0" v-model="formStreamNetworkValue.streamValueCell" style="width: 100px" ></Input> -->
                    <span>（单位：cell）</span>
                  </FormItem>
                  <FormItem prop='streamValueArea'>
                    <Input :min="0" v-model="formStreamNetworkValue.streamValueArea" style="width: 90px" @on-change="convertCellArea('area')"></Input>
                    <span>（单位：km<sup>2</sup>）</span>
                    <Button  @click="streamNetValueCommit" style="margin-left:3px">确定</Button>
                  </FormItem>
                
                </Form>
              </div>

              <Divider orientation="left">流域出口设置</Divider>
              <div>
                <RadioGroup v-model="outletType" style="margin:5px 0px 5px 28px" @on-change="changeOutletDataResource">
                  <Radio label="outletFromMapDraw" >
                    <span>使用绘图工具在页面上绘制</span>
                  </Radio>
                  <Radio label="outletFromLocal" style="padding-left:45px">
                    <span>使用本地上传的outlet数据</span>
                  </Radio>
                </RadioGroup><br>
              </div>
              <div class="selectFlex">
                  <div style="width:210px;margin-left:24px">
                    <span>在地图上指定流域出口</span>
                    <i-switch v-model="outletDrawSwitch" :disabled="outletType=='outletFromLocal'">
                      <Icon slot="open" type="md-pin" size="14"></Icon>
                      <Icon slot="close" type="md-close" size="14"></Icon>
                    </i-switch><br>
                    <div v-if="outletOnMap">
                      <span><Icon type="ios-checkmark-circle-outline" color="#19be6b" size="20"/>已选中流域出口</span>
                      <ButtonGroup size="small">
                        <Button type="success" ghost @click="basinOutletSelectApply" :loading="basinOutletSelectLoadingStatus">确定</Button>
                        <Button type="error" ghost @click="basinOutletReselect">重选</Button>
                      </ButtonGroup>
                    </div>
                  </div>
                  <div style="width:210px">
                    <Upload 
                      ref="outletUpload"
                      :show-upload-list='false'
                      :on-success="uploadSuccess"
                      :format="['shp','shx','dbf','prj']"
                      :on-format-error="handleFormatError"
                      :max-size="102400"
                      :on-exceeded-size="handleMaxSize"
                      :before-upload="handleBeforeUpload"
                      :multiple='true'
                      action="/upload">
                      <Button icon="ios-cloud-upload-outline" :disabled="outletType=='outletFromMapDraw'">上传outlet文件</Button>
                    </Upload>
                    <div v-if="uploadList.length > 0" >
                      <span>待上传文件:</span><br>
                      <Tag type="border" closable color="primary" 
                          v-for="(file,index) in uploadList" :key="index" :name="file.name" @on-close="outletUploadClose">
                        {{ file.name }}
                      </Tag>
                      <Button @click="upload" :loading="loadingStatus">{{ loadingStatus ? '上传中' : '点击上传' }}</Button>
                    </div>
                  </div>
              </div>
              
              <div class="finishBtn">
                <!-- <Button v-if="subbasinDelineationStatus=='modelSetting'" type="info" ghost @click="geoserverPublih">生成流域划分结果</Button> -->
                <Button v-if="subbasinDelineationStatus=='modelSetting'" type="info" ghost @click="sdDelineation">生成流域划分结果</Button>
                <div v-else-if="subbasinDelineationStatus=='modelRunning'" style="width:85%">
                  <i-progress :percent="99.99" status="active" hide-info></i-progress>
                  <span>流域划分运行中...</span>
                </div>
                <div v-else-if="subbasinDelineationStatus=='modelRunSuccess'" style="width:100%">
                  <p class="finishFont">流域划分结果已生成</p>
                  <div class="finishBtn">
                    <Button type="info" ghost @click="sdDelineation">重新生成流域划分结果</Button>
                  </div>
                </div>
              </div>
          </div>
        </TabPane>
        <!-- 2、HRU划分 模拟单元 -->
        <TabPane label="模拟单元划分">
          <div>
            <Divider orientation="left">土壤质地图</Divider>
            <span>选择土壤质地图来源</span>
            <br>
            <RadioGroup v-model="soilMapDataType" style="margin:5px 0px 5px 25px">
              <Radio label="soilMapFromSystem">
                <span>使用系统提供的土壤数据</span>
              </Radio>
              <Radio label="soilMapFromLocal" style="padding-left:46px">
                <span>使用本地上传的土壤数据</span>
              </Radio>
            </RadioGroup> 
            <!-- <span>使用系统提供的土壤质地图：</span> -->
            <div class="selectFlex">
              <div>
                <Select clearable v-model="soilMapSelect" style="width:200px" :disabled="soilMapDataType=='soilMapFromLocal'" placeholder="请选择土壤类型数据">
                  <Option v-for="item in soilMapList" :value="item.value" :key="item.value">{{ item.label }}</Option>
                </Select><br>
                <Select clearable v-model="soilParaSelect" style="width:200px" :disabled="soilMapDataType=='soilMapFromLocal'" placeholder="请选择土壤属性数据">
                  <Option v-for="item in soilParaList" :value="item.value" :key="item.value">{{ item.label }}</Option>
                </Select>
              </div>
              <!-- <Divider type="vertical" /> -->
              <!-- <span>使用本地土壤质地图：</span> -->
              <div>
                <Upload action="" class="upload">
                  <Button icon="ios-cloud-upload-outline" :disabled="soilMapDataType=='soilMapFromSystem'">上传土壤质地图</Button>
                </Upload><br>
                <Upload action="" class="upload">
                  <Button icon="ios-cloud-upload-outline" :disabled="soilMapDataType=='soilMapFromSystem'">上传Soil Lookup Table</Button>
                </Upload>
              </div>
            </div>
            <Divider orientation="left">土地利用数据</Divider>
            <span>选择土地利用数据来源</span>
            <br>
            <RadioGroup v-model="landuseDataType" style="margin:5px 0px 5px 17px">
              <Radio label="landuseFromSystem">
                <span>使用系统提供的土地利用数据</span>
              </Radio>
              <Radio label="landuseFromLocal" style="padding-left:17px">
                <span>使用本地上传的土地利用数据</span>
              </Radio>
            </RadioGroup><br>  
            <!-- <span>使用系统提供的土地利用数据：</span> -->
            <div class="selectFlex">
              <div>
                <Select clearable v-model="landuseMapSelect" style="width:200px" :disabled="landuseDataType=='landuseFromLocal'">
                  <Option v-for="item in landuseMapList" :value="item.value" :key="item.value">{{ item.label }}</Option>
                </Select>
              </div>
              <div>
                <Upload action="" class="upload">
                  <Button icon="ios-cloud-upload-outline" :disabled="landuseDataType=='landuseFromSystem'">上传土地利用图</Button>
                </Upload><br>
                <Upload action="" class="upload">
                  <Button icon="ios-cloud-upload-outline" :disabled="landuseDataType=='landuseFromSystem'">上传Landuse Lookup Table</Button>
                </Upload>
              </div>
            </div>
            <div class="finishBtn">
              <Button v-if="simulationUnitDelineationStatus=='modelSetting'" type="info" ghost @click="sdSimulationUnit">进行模拟单元划分</Button>
              <div v-else-if="simulationUnitDelineationStatus=='modelRunning'" style="width:85%">
                <i-progress :percent="99.99" status="active" hide-info></i-progress>
                <span>模拟单元划分运行中...</span>
              </div>
              <div v-else-if="simulationUnitDelineationStatus=='modelRunSuccess'" style="width:100%">
                <p class="finishFont">模拟单元已划分</p>
                <div class="finishBtn">
                  <Button type="info" ghost @click="sdSimulationUnit">重新进行模拟单元划分</Button>
                </div>
              </div>
              <!-- <Button type="info" ghost>进行模拟单元划分</Button> -->
              <!-- <Button type="info" ghost @click="sdSimulationUnit">进行模拟单元划分</Button> -->
            </div>
          </div>
        </TabPane>
        <!-- 3、气象数据 -->
        <TabPane label="气象数据">
          <Divider orientation="left">Rainfall Data</Divider>
          <span>选择降雨数据来源：</span>
          <RadioGroup v-model="rainDataType" style="margin:5px 0px 5px 17px">
            <Radio label="rainFromSystem">
              <span>使用系统提供的降雨数据</span>
            </Radio>
            <Radio label="rainFromLocal" style="padding-left:89px">
              <span>使用本地上传的降雨数据</span>
            </Radio>
          </RadioGroup>   
          <br>
          <!-- <span>使用系统提供的降雨数据：</span> -->
          <div class="selectFlex">
              <div>
                <Select v-model="landuseMapSelect" :transfer=true style="width:200px" :disabled="rainDataType=='rainFromLocal'">
                  <Option v-for="item in rainDataList" :value="item.value" :key="item.value">{{ item.label }}</Option>
                </Select>
               </div>
            <!-- <span>使用本地上传的降雨数据：</span> -->
            <div>
              <Upload action="" class="upload" >
                <Button icon="ios-cloud-upload-outline" :disabled="rainDataType=='rainFromSystem'">上传降雨数据</Button>
              </Upload>
            </div>
          </div>
          <Divider orientation="left"> Meteorological Data </Divider>
          <span>选择气象温度风速辐射数据来源：</span>
          <RadioGroup v-model="meteoDataType" style="margin:5px 0px 5px 17px">
            <Radio label="meteoFromSystem">
              <span>使用系统提供的气象数据</span>
            </Radio>
            <Radio label="meteoFromLocal" style="padding-left:89px">
              <span>使用本地上传的气象数据</span>
            </Radio>
          </RadioGroup>   
          <div class="selectFlex">
            <!-- <span>使用系统提供的气象数据：</span> -->
            <div>
              <Select v-model="meteoDataSelect" :transfer=true style="width:200px" :disabled="meteoDataType=='meteoFromLocal'">
                <Option v-for="item in meteoDataList" :value="item.value" :key="item.value">{{ item.label }}</Option>
              </Select>
            </div>
            <div>
              <!-- <span>使用本地上传的气象数据：</span> -->
              <Upload action="" class="upload">
                <Button icon="ios-cloud-upload-outline" :disabled="meteoDataType=='meteoFromSystem'">上传气象数据</Button>
              </Upload>
            </div>
          </div>
          <div class="finishBtn">
            <Button type="info" ghost>完成气象数据设置</Button>
          </div>
        </TabPane>
        <!-- 水文观测 -->
        <TabPane label="水文观测">
          <Divider orientation="left">水文观测数据</Divider>
          <span>观测数据类型：</span>
          <Tag v-for="item in obsDataTypeSelect" :key="item" :name="item" closable @on-close="handleCloseObs">{{ item }}</Tag>    
          <CheckboxGroup v-model="obsDataResource" style="margin:5px 0px 5px 10px">
            <Checkbox label="obsFromSystem">
                <span>观测数据选择：</span>
            </Checkbox>
            <Checkbox label="obsFromLocal" style="padding-left:90px">
                <span>使用本地上传的观测数据：</span>
            </Checkbox>
          </CheckboxGroup>
          <div class="selectFlex">
            <div style="margin:0px 10px">
                <Select 
                  v-model="obsDataTypeSelectSystem" 
                  :transfer=true style="width:200px" 
                  @on-change='addObsDataTypeSelectSystem' 
                  :disabled='obsSelectShow(obsFromSystem)'>
                  <Option v-for="item in obsDataTypeList" :value="item" :key="item">{{ item }}</Option>
                </Select>
            </div>
            <div style="margin:0px 10px ">
                <Input v-model="obsDataTypeLocal.name" placeholder="请输入观测数据类型名" :disabled='obsSelectShow(obsFromLocal)' style="width:180px"></Input>
                <Button @click="obsDataTypeLocalSubmit" :disabled='obsSelectShow(obsFromLocal)'>添加</Button>
                <Upload action="" class="upload">
                  <Button icon="ios-cloud-upload-outline" :disabled='obsSelectShow(obsFromLocal)' style="margin-top:10px ">上传本地观测数据</Button>
                </Upload>
            </div>
          </div>
          <br>
        </TabPane>
          <!-- todo 
          1、修改样式、按钮颜色区分
          3、字体
          4、流域出口指定选项
          5、新建project页面
           -->
      </Tabs>
    </div>
  </Modal>
</template>

<script>
import axios from 'axios';
import {request} from "@/network/request";
import {requestGis} from "@/network/request";
export default {
  name: 'CreateModel',
  props:{
    createModelShow: {
        type: Boolean
    }
  },
  watch:{
    createModelShow(newVal, oldVal){
      this.createModelShowTemp=newVal
    },
    immediate: true
  },
  created(){
 
    this.initLevelLabel();
  },
  mounted(){
    this.mapCM=this.$store.getters.storeMap;
    this.initController();
    this.listenDraw();
  },
  data() {
      const validateProjectName = (rule, value, callback) =>{
        if (value) {
          if (!value.match(/^[\u4E00-\u9FA5A-Za-z0-9_]+?$/)) {
            callback(new Error('不可输入特殊字符'))
          } else if(value.length>16){
            callback(new Error('项目名称字符应少于16位'))
          }else{
              callback()
          }
        }
      };
      const validateStreamValueCell = (rule, value, callback) =>{
        if (!value.match(/^\+?[1-9][0-9]*$/)) {
            callback(new Error('应输入非零正整数'))
          }else{
            callback()
          }
      };
      const validateStreamValueArea = (rule, value, callback) =>{
        // if (!value.match(/^[0-9]+(\.[0-9]{1,6})?$/)) {
        if (!value.match(/^(([1-9]\d{0,6})|0)(\.\d{0,5})?$/)) {
            callback(new Error('阈值应大于零,且有最多5位小数'))
          }else{
            callback()
          }
      };

      return {
        createModelShowTemp:false,
        formProjectItem:{
          projectName:'',
        },
        ruleProject:{
          projectName:[
            { required: true, message: '项目名称不能为空', trigger: 'change' },
            {type: 'string',validator: validateProjectName, trigger: 'change'}
          ]
        },
        ///////
        mapCM: null,
        drawingLayerGroup: null,
        drawingBasinLayerGroup: null,
        basinScopeType:'basinScopeFromSystem',
        basinCkb:false,
        levelSelect: 7,
        levelLayerList:[],
        basinLayer: null,
        querySwitch:false,
        basinScopeSelectSystem:false,
        basinScopeSelectLoadingStatus:false,
        DEMDataSize:90,
        formStreamNetworkValue:{
          streamValueArea:'1.62',
          streamValueCell:'200',
        },
        ruleStreamNetwork:{
          streamValueCell:[
            { required: true, message: '河网阈值不能为空', trigger: 'blur' },
            // {type:'number',pattern:/^\+?[1-9][0-9]*$/,message:'应输入非零正整数', trigger: 'blur',} //transform(value) {return Number(value);}
            {type:'number', validator:validateStreamValueCell, trigger: 'blur',}
          ],
          streamValueArea:[
            { required: true, message: '河网阈值不能为空', trigger: 'blur' },
            // {type:'number', pattern:/^[0-9]+(\.[0-9]{1,3})?$/,message:'阈值应大于零', trigger: 'blur' }
            {type:'number', validator:validateStreamValueArea, trigger: 'blur',} 
          ]
        },
        outletType:'outletFromMapDraw',
        outletDrawSwitch:false,
        outletGeoJson:null,
        uploadList: [],
        outletMaxLength:4,
        uploadMaxLength:1,
        loadingStatus:false,
        outletOnMap:false,
        basinOutletSelectLoadingStatus:false,
        subbasinDelineationStatus:'modelSetting',
        ////////
        soilMapList:[
          {
            value:'ChinaSoilType1km',
            label:'中国土壤类型图'
          },
          {
            value:'soil-2',
            label:'soil-2'    
          },
        ],
        soilMapSelect:'',
        soilMapDataType:'soilMapFromSystem',
        soilParaSelect:'',
        soilParaList:[
          {
            value:'SGW_china',
            label:'中山大学上官微土壤属性数据集'
          },
          {
            value:'soilgrids',
            label:'soilgrids土壤属性数据集'
          }
        ],
        landuseMapList:[
          {
            value:'ESA_CCI_300m',
            label:'ESA-CCI-300m'
          },
          {
            value:'landuse-2',
            label:'landuse-2'    
          },
        ],
        landuseMapSelect:'',
        landuseDataType:'landuseFromSystem',
        simulationUnitDelineationStatus:'modelSetting',
        ///////////////
        rainDataType:'rainFromSystem',
        rainDataList:[
          {
            value:'rain-1',
            label:'rain-1'
          },
          {
            value:'rain-2',
            label:'rain-2'    
          },
        ],
        rainDataSelect:'',
        meteoDataType:'meteoFromSystem',
        meteoDataList:[
          {
            value:'meteo-1',
            label:'meteo-1'
          },
          {
            value:'meteo-2',
            label:'meteo-2'    
          },
        ],
        meteoDataSelect:'',
        //////////////
        obsDataTypeList:[
          'Q', 'SED', 'NO3', 'NH4'
        ],
        obsDataTypeSelect:[],
        obsDataTypeSelectSystem:[],
        obsDataTypeLocal:{
          name:'',
        },
        obsDataResource:[],
        obsFromSystem:'obsFromSystem',
        obsFromLocal:'obsFromLocal'
      }
  },
  methods: {
    namedProject(name){
      this.$refs[name].validate((valid) => {
        if (valid) {
          let params = new URLSearchParams();
          params.append("newProjectName",this.formProjectItem.projectName);
          request
          .post('/project/create',params)
          .then(res=>{ 
            if (res.data.errCode == 200) {
              this.$Message.success({
                content:'项目命名成功',
                duration: 3
              });
              this.$store.commit("SET_PROJECT_NAME", this.formProjectItem.projectName);
            } else {
                this.$Message.error({
                content:'项目命名失败，请重新命名',
                duration: 3,
                });
                this.formProjectItem.projectName='';
            }
          })
          .catch(e =>  {
            this.$Message.error('项目命名失败，请重新命名');
          });
        } else {
            this.$Message.error({
            content:'项目命名失败，请重新命名',
            duration: 3,
            });
            this.formProjectItem.projectName='';
        }
      })
    },
    handleSubmitProject (name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          this.$Message.success('Success!');
        } else {
          this.$Message.error('Fail!');
        }
      })
    },
    initController(){
      this.drawingLayerGroup = L.layerGroup([]);
      this.drawingLayerGroup.addTo(this.mapCM);
      this.drawingBasinLayerGroup = L.layerGroup([]);
      this.drawingBasinLayerGroup.addTo(this.mapCM);
      var options = {
        position: "topright", // toolbar position, options are 'topleft', 'topright', 'bottomleft', 'bottomright'
        drawMarker: true, // adds button to draw markers
        drawCircleMarker:false,
        drawPolyline: false, // adds button to draw a polyline
        drawRectangle: false, // adds button to draw a rectangle
        drawPolygon: false, // adds button to draw a polygon
        drawCircle: false, // adds button to draw a cricle
        cutPolygon: false, // adds button to cut a hole in a polygon
        editMode: true, // adds button to toggle edit mode for all layers
        dragMode: false,
        removalMode: true // adds a button to remove layers
      };
      this.mapCM.pm.addControls(options);
    },
    listenDraw(){
      // console.log('listenDraw Begin');
      var _this = this;
      this.mapCM.on('pm:create', e=>{
        if (_this.outletDrawSwitch) {
          _this.drawingLayerGroup.clearLayers();
          _this.drawingLayerGroup.addLayer(e.layer);
          _this.outletGeoJson = e.layer._latlng//.toGeoJSON()
          console.log('create outlet point');
          _this.outletOnMap=true;
        }
      });
      this.mapCM.on('pm:remove', e=>{
        _this.drawingLayerGroup.clearLayers();
        _this.outletOnMap=false;
      });
      this.mapCM.on('click', e=>{
        if(_this.querySwitch){
            var latlng = e.latlng;
            var lon = latlng['lng'];
            var lat = latlng['lat'];
            var baseUrl="";
            baseUrl = "/basins/querySubLevel/" + this.levelSelect + "?projectName=" + this.formProjectItem.projectName +"&lon=" + lon +"&lat=" + lat;
            this.$Spin.show();
            request
            .get(baseUrl)
            .then(res=>{
              if(res.data!=0){
                var tempGeoJSON = res.data;
                _this.addGeoJSONToMap(JSON.stringify(tempGeoJSON), "red");
                _this.$Spin.hide();
                _this.basinScopeSelectSystem=true;
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
    initLevelLabel(){
      for(var i=2;i<11;i++){
        var item = {
          value: i,
          label: 'level ' + i
        }
        this.levelLayerList.push(item);
      }
    },
    showBasinsLayer(show){
      if(show){
        this.changeStandardBasinsLevel()
        console.log('Show Basin Layer');
      }
      else{
        console.log('Hide Basins Layer.');
        this.querySwitch=false;
        this.querySwitchChange(false);
        try{
            this.basinLayer.remove();
        }catch{}
      }
    },
    changeStandardBasinsLevel(){
      try{
          this.basinLayer.remove();
      }catch{}
      var level = this.levelSelect;
      //   this.basinLayer = L.tileLayer.wms('http://210.26.48.56:30122/geoserver/Basin_shp/wms',{
      this.basinLayer = L.tileLayer.wms('http://223.2.40.37:7070/geoserver/Basin_shp/wms',{
          layers: 'Asia_level_0' + level,
          format: 'image/png',
          transparent: true,
          noWarp:true
      }).addTo(this.mapCM);
      console.log('Show Standard Basin Layer in Level ' + level + '.');
    },
    changeBasinScopeDataResource(){
      if (this.basinScopeType!=='basinScopeFromSystem') {
        this.basinCkb=false;
        this.querySwitch=false;
        try{
          this.basinLayer.remove();
          this.showBasinsLayer(false)
        }catch{}
      } 
    },
    addGeoJSONToMap(GeoJSONStr, color){
      this.drawingBasinLayerGroup.clearLayers();
      var file = JSON.parse(GeoJSONStr);
      var geoJsonLayer = L.geoJSON(file, {
          style: function(feature) {
              return { color: color , fillColor: color, fillOpacity:0};
          }
      }).bindPopup(function(layer) {
          return layer.feature.properties.description;
      });
      this.loadFeatures(geoJsonLayer);
      //平移至数据位置
      this.mapCM.fitBounds(geoJsonLayer.getBounds());
    },
    loadFeatures(featureCollection) {
      featureCollection.eachLayer(layer => {
          this.drawingBasinLayerGroup.addLayer(layer);
      });
    },
    removeBasinLayer(){
      console.log('Select local data, remove basin layer');
      try{
        this.basinLayer.remove();
      }catch{}
    },
    basinScopeSelectSystemReselect(){
      this.drawingBasinLayerGroup.clearLayers();
      this.basinScopeSelectSystem=false;
    },
    basinScopeSelectSystemApply(){
      //请求后端geojson转shp
      this.basinScopeSelectLoadingStatus=true;
      console.log("basin scope selected apply");
      let params = new URLSearchParams();
      params.append("projectName",this.formProjectItem.projectName);
      request.post('/basins/preprocess/geojson2shp',params)
      .then(res=>{
        if (res.data.errCode == 200) {
              this.$Notice.success({
                title:'选择流域范围成功！',
                duration: 5
              });
              this.querySwitch=false;
        } else {
            this.$Notice.error({
            title:'选择流域范围失败，请重新选择！',
            duration: 5,
            });
          }
      }).catch(e=>{
          this.$Notice.error({
          title:'选择流域范围失败，请重新选择！',
          duration: 5,
          });
      })
      window.setTimeout(()=>{
        this.basinScopeSelectLoadingStatus=false;
      }, 5000);
    },
    querySwitchChange(status){
      if (status) {
        
      } else {
        this.basinScopeSelectSystem=false;
        this.drawingBasinLayerGroup.clearLayers();
      }
    },
    streamNetValueCommit(){
      let params = new URLSearchParams();
      params.append("projectName",this.formProjectItem.projectName);
      params.append("streamNetValue",this.formStreamNetworkValue.streamValueCell);
      request.post('/basins/preprocess/streamNetworkValue',params)
      .then(res=>{
        if (res.data.errCode == 200) {
          this.$Notice.success({
            title:'河网阈值设置成功',
            duration:10
          })
        }else {
          this.$Notice.warning({
            title: '河网阈值设置失败，请重新设置！',
            desc: '失败原因： ' + res.data.msg,
            duration:10
          });
        }
      }).catch(e => {
          this.$Notice.error({
            title: '河网阈值设置失败，请重新设置！',
          });
      })
      // this.handleSubmitProject('formStreamNetworkValue')
    },
    uploadSuccess(res, file) {
      if (res.errCode == 200) {
        this.$Notice.success({
          title:'文件上传成功',
          duration:10
        })
      } else {
        this.$Notice.warning({
          title: '文件上传失败',
          desc: '失败原因： ' + res.msg,
          duration:10
        });
      }
    },
    handleFormatError(file) {
      this.$Notice.warning({
        title: '文件格式不正确',
        desc: file.name + '的文件格式不对，请重新选择文件进行上传。',
        duration:10
      });
    },
    handleMaxSize(file) {
      this.$Notice.warning({
        title: '超过文件大小限制',
        desc: '文件  ' + file.name + ' 超过100MB.'
      });
    },
    handleBeforeUpload(file) {
      this.uploadList.push(file);
      const FileExt = file.name.replace(/.+\./, "");//取得文件的后缀名
      const check = this.uploadList.length <= this.outletMaxLength;
      if (!check) {
        this.$Notice.warning({
          title: '最多只能上传' + this.outletMaxLength + '个文件.',
          duration:10
        });
      }else if (['shp','shx','dbf','prj'].indexOf(FileExt.toLowerCase()) === -1) { //
        this.$Notice.error({title:'请上传以shp,shx,dbf,prj结尾的文件', duration:10});
        var index = this.uploadList.indexOf(file.name);
        this.uploadList.splice(index, 1);
      }else if (file.size > 102400){
        this.$Notice.warning({
          title: '超过文件大小限制',
          desc: '文件  ' + file.name + ' 超过100MB.',
          duration:10
        });
        var index = this.uploadList.indexOf(file.name);
        this.uploadList.splice(index, 1);
      }
      return false;
    },
    upload(){
      this.loadingStatus=true;
      let requestConfig = {
        headers: {
          'Content-Type': 'multipart/form-data'
        },
      }
      for(let i = 0; i < this.uploadList.length; i++) {
        let fileFormData = new FormData();
        fileFormData.append('file', this.uploadList[i]);
        request
        .post('/file/upload', fileFormData, requestConfig)
        .then(res=>{
          if (res.data.errCode == 200) {
            this.$Notice.success({
              title:this.uploadList[i].name + '上传成功',
              duration:10
            })
          }else {
            this.$Notice.warning({
              title: this.uploadList[i].name + '上传失败',
              desc: '失败原因： ' + res.data.msg,
              duration:10
            });
          }
        }).catch(e => {
            this.$Notice.error({
              title: this.uploadList[i].name + '上传失败'
            });
          })
        }
        window.setTimeout(()=>{
          this.loadingStatus=false;
        }, 3000);
    },
    outletUploadClose(event,name){
      for(var i=this.uploadList.length-1;i>=0;i--){
        if(this.uploadList[i].name==name)
        {
          this.uploadList.splice(i,1);
        }
      }
    },
    changeOutletDataResource(){
      if (this.outletType==='outletFromMapDraw') {
        this.uploadList=[]
      } else if(this.outletType==='outletFromLocal'){
        this.outletGeoJson=null;
        this.outletDrawSwitch=false;
        this.outletOnMap=false;
        this.drawingLayerGroup.clearLayers();
      }
    },
    outletPointShp(lat,lon){
      let params = new URLSearchParams();
      params.append("projectName",this.formProjectItem.projectName);
      params.append("lat",lat);
      params.append("lon",lon);
      request.post('/basins/preprocess/outletShp',params)
      .then(res=>{
        if (res.data.errCode == 200) {
          this.$Notice.success({
            title:'outlet设置成功',
            duration:10
          })
        }else {
          this.$Notice.warning({
            title: 'outlet设置失败，请重新设置！',
            desc: '失败原因： ' + res.data.msg,
            duration:10
          });
        }
      }).catch(e => {
          this.$Notice.error({
            title: 'outlet设置失败，请重新设置！',
          });
      })
      window.setTimeout(()=>{
        this.basinOutletSelectLoadingStatus=false;
      }, 3000);
    },
    basinOutletSelectApply(){
      this.basinOutletSelectLoadingStatus=true;
      this.outletPointShp(this.outletGeoJson.lat,this.outletGeoJson.lng);
    },
    basinOutletReselect(){
      this.drawingLayerGroup.clearLayers();
      this.outletOnMap=false;
    },
    sdDelineation(){
      this.subbasinDelineationStatus='modelRunning';
      let params = new URLSearchParams();
      params.append("projectName",this.formProjectItem.projectName);
      requestGis.post('/ws-hydro/SEIMSDataProcessing/sdDelineation',params)
      .then(res=>{
        console.log(res);
        if (res.status == 200) {
          this.$Notice.success({
            title:'流域划分结果已生成',
            duration:10
          })
        this.subbasinDelineationStatus='modelRunSuccess';
        // this.geoserverPublih();
        }else {
          this.$Notice.warning({
            title: '流域划分失败，请重新设置！',
            desc: '失败原因： ' + res.data.msg,
            duration:10
          });
          this.subbasinDelineationStatus='modelSetting';
        }
      }).catch(e => {
          this.$Notice.error({
            title: '流域划分失败，请重新设置！',
          });
          this.subbasinDelineationStatus='modelSetting';
      })
    },
    sdSimulationUnit(){
      var _this = this
      _this.simulationUnitDelineationStatus='modelRunning';
      let params = new URLSearchParams();
      params.append("projectName",_this.formProjectItem.projectName);
      // _this.simulationUnitParams(_this.landuseMapSelect,'simulationUnitLanduse')
      axios.all([_this.simulationUnitParams(_this.landuseMapSelect,'simulationUnitLanduse'), 
                  _this.simulationUnitParams(_this.soilMapSelect, 'simulationUnitSoiltype')])
      .then(axios.spread(function(landuseResp, soilResp){
      // .then(e=>{
        // if (e.data.errCode == 200 ) {
        if (landuseResp.data.errCode == 200 && soilResp.data.errCode == 200) {
              _this.$Notice.success({
              title: '选择模拟单元数据成功',
              duration:10
            })
          _this.sdSimulationUnitSeimsApi();
          _this.soilLookupTableSeimsApi();
        } else {
            _this.$Notice.warning({
              title: '选择模拟单元数据失败,请重新选择',
              duration:10
            })
            _this.simulationUnitDelineationStatus='modelSetting';
        }
      }));
    },
    simulationUnitParams(selectedData,urlType){
      let params = new URLSearchParams();
      params.append("projectName",this.formProjectItem.projectName);
      params.append("selectedData",selectedData);
      if (urlType =='simulationUnitSoiltype') {
        params.append("soilParaSelectedData",this.soilParaSelect);
      }
      var url = '/basins/preprocess/' + urlType
      return request.post(url,params)
    },
    soilLookupTableSeimsApi(){
      let params = new URLSearchParams();
      params.append("projectName",this.formProjectItem.projectName);
      requestGis.post('/ws-hydro/SEIMSDataProcessing/soilLookupTable',params)
      .then(res=>{
        console.log(res);
        if (res.status == 200) {
          this.$Notice.success({
            title:'Soil Lookup Table 已生成',
            duration:10
          })
        // this.geoserverPublih();
        }else {
          this.$Notice.warning({
            title: 'Soil Lookup Table 生成失败，请重新设置！',
            desc: '失败原因： ' + res.data.msg,
            duration:10
          });
          this.simulationUnitDelineationStatus='modelSetting';
        }
      }).catch(e => {
          this.$Notice.error({
            title: 'Soil Lookup Table 生成失败，请重新设置！',
          });
          this.simulationUnitDelineationStatus='modelSetting';
      })
    },
    sdSimulationUnitSeimsApi(){
      let params = new URLSearchParams();
      params.append("projectName",this.formProjectItem.projectName);
      requestGis.post('/ws-hydro/SEIMSDataProcessing/sdField',params)
      .then(res=>{
        // console.log(res);
        if (res.status == 200) {
          this.$Notice.success({
            title:'流域划分结果已生成',
            duration:10
          })
          this.simulationUnitDelineationStatus='modelRunSuccess';
        // this.geoserverPublih();
        }else {
          this.$Notice.warning({
            title: '流域划分失败，请重新设置！',
            desc: '失败原因： ' + res.data.msg,
            duration:10
          });
          this.simulationUnitDelineationStatus='modelSetting';
        }
      }).catch(e => {
          this.$Notice.error({
            title: '流域划分失败，请重新设置！',
          });
          this.simulationUnitDelineationStatus='modelSetting';
      })
    },
    geoserverPublih(){
      var paramObj = {
        fileName: "D:/WEB/basins/test/test7/workspace/spatial_raster/dem.tif",
        workSpace: this.formProjectItem.projectName,
        styleName: "dem",
        dataSetName: "dem",
        layerName: "dem",
        dataType: 2,
        crs:4326
      };
      var options = {
        headers: {
          "content-type": "application/json"
        },
        method: "POST",
        data: JSON.stringify(paramObj)
      };
      request('/geoserver/publish',options)
      .then(res=>{
        console.log(res);
        if (res.data.errCode == 200) {
          console.log("gerosrver publish success");
        }else {
          console.log("gerosrver publish failed");
        }
      }).catch(e => {
          console.log("gerosrver publish failed");
      })
    },
    transformDecimal(number, i) {
      let decimalNum = null;
      // 先转换为数值型
      let num = Number(number);
      // 判断是否为数值型
      if(!isNaN(num)) {
          // 切分整数与小数
          let arr = num.toString().split(".");
          // 是小数且小数位大于保留个数
          if(arr.length > 1 && arr[1].length > i) {            
              // 小数部分字符串
              let decimal = arr[1].slice(i, i+1);
              // toFixed 有 bug，四舍六入五随机
              // 当四舍五入的数为 5，给其 + 1
              if(decimal === '5') {
                  // 这里可能会存在 0.1 ** 5 = 0.000010000000000000003 但不影响四舍五入
                  num += Math.pow(0.1, i+1);
              }
              decimalNum = num.toFixed(i);        
          }
          else {
              decimalNum = num;
          }
          decimalNum = Number(decimalNum);
    }     
    return String(decimalNum);
    },
    convertCellArea(type){
      var streamValueArea = this.formStreamNetworkValue.streamValueArea;
      var streamValueCell = this.formStreamNetworkValue.streamValueCell;
      if (type=='cell') {
        var valueAreaTmp = streamValueCell * Math.pow(this.DEMDataSize,2) *0.000001
        this.formStreamNetworkValue.streamValueArea = this.transformDecimal(valueAreaTmp, 4)  
      } else if (type=='area') {
        var valueCellTmp = (streamValueArea*1000000)/Math.pow(this.DEMDataSize,2);
        this.formStreamNetworkValue.streamValueCell = this.transformDecimal(valueCellTmp,0)
      }
    },
    handleCloseObs (event, name) {
      const index = this.obsDataTypeSelect.indexOf(name);
      this.obsDataTypeSelect.splice(index, 1);
    },
    obsDataTypeLocalSubmit(){
      var obsName=this.obsDataTypeLocal.name;
      console.log(obsName);
      console.log(this.getBLen(obsName));
      console.log(this.obsDataTypeSelect.indexOf(obsName));
      if (this.getBLen(obsName) && this.obsDataTypeSelect.indexOf(obsName)<0) {
        this.obsDataTypeSelect.push(obsName);
      }
    },
    addObsDataTypeSelectSystem(val){
      if (val in this.obsDataTypeSelect) {
        
      }else{
        this.obsDataTypeSelect.push(val);
      }

    },
    getBLen(str) {
      if (str == null) return 0;
      if (typeof str != "string"){
        str += "";
      }
      return str.replace(/[^\x00-\xff]/g,"01").length;
    },
    obsSelectShow(obsName) {
      if (this.obsDataResource.indexOf(obsName)<0) {
        return true
      }else{
        return false
      }
    }
  },
}
</script>

<style >
  /* .ivu-divider-horizontal.ivu-divider-with-text-left{
      margin: 16px 0px 10px 0px;
  } */
  .ivu-btn {
    padding: 0px 9px;
  }
  .ivu-tabs-nav-wrap{
    text-align: center;
  }
  .ivu-tabs-nav-scroll{
    display: inline-block;
  }
  .upload {
    display: inline-block;
  }
  .selectFlex {
    display: flex;
    flex-direction: row ;
    justify-content: space-around ;
  }    
  .selectFlexBasin {
    display: flex;
    flex-direction: row ;
    justify-content: center ;
  } 
  .finishBtn{
    display: flex;
    justify-content: center ;
    margin-top: 7px;
    text-align: center;
  }
  .finishBtn>button{
    width: 90%;
  }
  .finishFont{
    color: #19be6b;
  }
   /* #streamValueForm .ivu-form-item {
    margin-bottom: 5px;
  } */

</style>
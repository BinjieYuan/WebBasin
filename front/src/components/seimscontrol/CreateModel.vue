<!--
 * @Author: your name
 * @Date: 2022-04-28 13:53:55
 * @LastEditTime: 2022-05-04 16:12:16
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \WebBasin\front\src\components\seimscontrol\CreateModel.vue
-->
<template>
  <Modal v-model="createModelShowTemp" draggable scrollable :mask="false" title="SEIMS 建模">
    <div>
    <!--1、流域划分-->
      <Tabs type="card" :animated="false" >
        <TabPane label="流域划分">
          <div>
              <Divider orientation="left">建模流域范围</Divider>
              <!-- <h3>建模流域范围</h3> -->
              <div class="selectFlex">
                <Button>选择流域范围</Button>
                <Upload action="" class="upload">
                  <Button icon="ios-cloud-upload-outline">上传DEM数据</Button>
                </Upload>
              </div>
              <Divider orientation="left">生成河网</Divider>
              <div style="margin:0 34px">
                <span>输入生成河网阈值： </span>
                <InputNumber :min="0" v-model="streamValue" style="width: 50px"></InputNumber>
                <span>（单位：cell）</span>
                <Button>计算河网</Button>
              </div>

              <Divider orientation="left">流域出口设置</Divider>
              <div class="selectFlex">
                  <div>
                    <span>指定流域出口</span>
                  </div>
                  <div>
                    <Upload action="" class="upload">
                      <Button icon="ios-cloud-upload-outline">上传outlet文件</Button>
                    </Upload>
                    <Button>在地图上指定流域出口</Button><br>
                  </div>
              </div>
              

              <div class="finishBtn">
                <Button >生成流域划分结果</Button>
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
                <span>使用系统提供的土壤属性图</span>
              </Radio>
              <Radio label="soilMapFromLocal" style="padding-left:46px">
                <span>使用本地上传的土壤属性图</span>
              </Radio>
            </RadioGroup> 
            <!-- <span>使用系统提供的土壤质地图：</span> -->
            <div class="selectFlex">
              <div>
                <Select v-model="soilMapSelect" style="width:200px" :disabled="soilMapDataType=='soilMapFromLocal'">
                  <Option v-for="item in soilMapList" :value="item.value" :key="item.value">{{ item.label }}</Option>
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
                <Select v-model="landuseMapSelect" style="width:200px" :disabled="landuseDataType=='landuseFromLocal'">
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
              <Button>完成模拟单元划分</Button>
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
            <Button>完成气象数据设置</Button>
          </div>
        </TabPane>
        <!-- 水文观测 -->
        <TabPane label="水文观测">
          <Divider orientation="left">水文观测数据</Divider>
          <span>观测数据类型：</span>
          <Tag v-for="item in obsDataTypeSelect" :key="item" :name="item" closable @on-close="handleClose2">{{ item }}</Tag>    
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
  data() {
      return {
        createModelShowTemp:false,
        streamValue:35,
        ////////
        soilMapList:[
          {
            value:'soil-1',
            label:'soil-1'
          },
          {
            value:'soil-2',
            label:'soil-2'    
          },
        ],
        soilMapSelect:'',
        soilMapDataType:'soilMapFromSystem',
        landuseMapList:[
          {
            value:'landuse-1',
            label:'landuse-1'
          },
          {
            value:'landuse-2',
            label:'landuse-2'    
          },
        ],
        landuseMapSelect:'',
        landuseDataType:'landuseFromSystem',
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
    handleClose2 (event, name) {
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
  .finishBtn{
    display: flex;
    justify-content: flex-end ;
    margin-top: 7px;
  }

</style>
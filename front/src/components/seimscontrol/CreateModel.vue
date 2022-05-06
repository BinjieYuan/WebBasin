<!--
 * @Author: your name
 * @Date: 2022-04-28 13:53:55
 * @LastEditTime: 2022-05-06 20:32:05
 * @LastEditors: Please set LastEditors
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
                  <Button @click="handleSubmitProject('formProjectItem')">确定</Button>
                </FormItem>
                <!-- <FormItem>
                  <Button @click="handleSubmitProject('formProjectItem')">确定</Button>
                </FormItem> -->
              </Form>
            </div>
            <div class="finishBtn">
              <Button>下一步</Button>
            </div>
          </div>
        </TabPane>   
        <!--1、流域划分-->
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
                <Form id="streamValueForm" :model="formStreamNetworkValue" :label-width="180" :rules="ruleStreamNetwork" ref="formStreamNetworkValue" inline>  
                  <FormItem  label="输入生成河网阈值：" prop='streamValueCell'>
                    <!-- <InputNumber :min="0" v-model="formStreamNetworkValue.streamValueCell" style="width: 50px"></InputNumber> -->
                    <Input :min="0" v-model="formStreamNetworkValue.streamValueCell" style="width: 100px" @on-change="convertCellArea('cell')"></Input>
                    <!-- <Input :min="0" v-model="formStreamNetworkValue.streamValueCell" style="width: 100px" ></Input> -->
                    <span>（单位：cell）</span>
                  </FormItem>
                  <FormItem prop='streamValueArea'>
                    <Input :min="0" v-model="formStreamNetworkValue.streamValueArea" style="width: 100px" @on-change="convertCellArea('area')"></Input>
                    <span>（单位：km<sup>2</sup>）</span>
                  </FormItem>
                  <Button @click="handleSubmitProject('formStreamNetworkValue')">确定</Button>
                    <!-- <span>输入生成河网阈值： </span>
                    <InputNumber :min="0" v-model="streamValueCell" style="width: 50px"></InputNumber>
                    <span>（单位：cell）</span>
                    <Input  :min="0" v-model="streamValueArea" style="width: 50px"></Input>
                    <span>（单位：km<sup>2</sup>）</span>
                    <Button>计算河网</Button> -->
                </Form>
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
    handleSubmitProject (name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          this.$Message.success('Success!');
        } else {
          this.$Message.error('Fail!');
        }
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
  .finishBtn{
    display: flex;
    justify-content: flex-end ;
    margin-top: 7px;
  }
  
   #streamValueForm .ivu-form-item {
    margin-bottom: 5px;
  }

</style>
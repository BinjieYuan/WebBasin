<!--
 * @Author: your name
 * @Date: 2021-12-28 16:13:00
 * @LastEditTime: 2022-05-08 22:40:18
 * @LastEditors: BinjieYuan yuanbj9035@163.com
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \flaskVueSEIMS\client\src\components\MainLayer.vue
-->

<template>
    <div class="layout">
        <Header>
            <Menu mode="horizontal" theme="dark" active-name="1">
                <div class="layout-logo"></div>
                <div class="layout-nav">
                    <MenuItem name="1">
                        <Icon type="ios-navigate"></Icon>
                        Item 1
                    </MenuItem>
                    <MenuItem name="2">
                        <Icon type="ios-keypad"></Icon>
                        Item 2
                    </MenuItem>
                    <MenuItem name="3">
                        <Icon type="ios-analytics"></Icon>
                        Item 3
                    </MenuItem>
                    <MenuItem name="4">
                        <Icon type="ios-paper"></Icon>
                        Item 4
                    </MenuItem>
                </div>
            </Menu>
        </Header>
        <Layout>
            <Sider ref="side1" hide-trigger :style="{background: '#fff', width: '130px',minWidth:'130px', maxWidth: '130px',flex:' 0 0 130px'}" >
                <Menu ref="menuActive" :active-name="active" theme="light" width="auto" >
                    <MenuItem name="/basinshow" @click.native="showBasinShow">
                        <Icon type="ios-navigate"></Icon>
                        <span>流域展示</span>
                    </MenuItem>
                    <MenuItem name="/createmodel" @click.native="showCreateModel">
                        <Icon type="ios-search"></Icon>
                        <span >建模</span>
                    </MenuItem>
                    <MenuItem name="1-3">
                        <Icon type="ios-settings"></Icon>
                        <span>模拟</span>
                    </MenuItem>
                    <MenuItem name="1-3">
                        <Icon type="ios-settings"></Icon>
                        <span>率定</span>
                    </MenuItem>
                    <MenuItem name="1-3">
                        <Icon type="ios-settings"></Icon>
                        <span>开发中.....</span>
                    </MenuItem>
                </Menu>
            </Sider>
        
            <Layout>
                <!-- <Header :style="{padding: 0}" class="layout-header-bar">
                    <Icon @click.native="collapsedSider" :class="rotateIcon" :style="{margin: '0 20px'}" type="md-menu" size="24"></Icon>
                </Header> -->
                <Content :style="{margin: '10px', background: '#fff', Height: '260px'}">
                    <MapView></MapView>
                    <basin-show :basinShow.sync="basinShow"></basin-show>
                    <create-model :createModelShow.sync="createModelShow"></create-model>
                        <GeoDataShowControl></GeoDataShowControl>  
                </Content>
            </Layout>
        </Layout>
    </div>
</template>

<script>

import MapView from '../MapView.vue'
import BasinShow from '../mapcontrol/BasinShow.vue'
import CreateModel from '../seimscontrol/CreateModel.vue'
import GeoDataShowControl from '../mapcontrol/GeoDataShowControl.vue'
    export default {
        components:{MapView, BasinShow, CreateModel, GeoDataShowControl},
        data(){
            return {
                active: "/basinshow",
                basinShow:false,
                createModelShow:false,
            }
        },
        mounted(){

        },
        updated(){



        },
        methods: {
            showBasinShow(){
                this.basinShow=!this.basinShow,
                this.createModelShow=false
            },
            showCreateModel(){
                this.createModelShow=!this.createModelShow,
                this.basinShow=false
            }
        },
    }
</script>

<style scoped>
    .layout{
      border: 1px solid #d7dde4;
      background: #f5f7f9;
      position: relative;
      border-radius: 4px;
      overflow: hidden;
    }
    .layout-logo{
      width: 100px;
      height: 30px;
      background: #5b6270;
      border-radius: 3px;
      float: left;
      position: relative;
      top: 15px;
      left: 20px;
    }
    .layout-nav{
      width: 420px;
      margin: 0 auto;
      margin-right: 20px;
    }
    .layout-header-bar{
      background: #fff;
      box-shadow: 0 1px 1px rgba(0,0,0,.1);
    }
    .layout-logo-left{
      width: 90%;
      height: 30px;
      background: #5b6270;
      border-radius: 3px;
      margin: 15px auto;
    }
    .menu-icon{
      transition: all .3s;
    }
    .rotate-icon{
      transform: rotate(-90deg);
    }
    .menu-item span{
      display: inline-block;
      overflow: hidden;
      width: 75px;
      text-overflow: ellipsis;
      white-space: nowrap;
      vertical-align: bottom;
      transition: width .2s ease .2s;
    }
    .menu-item i{
      transform: translateX(0px);
      transition: font-size .2s ease, transform .2s ease;
      vertical-align: middle;
      font-size: 16px;
    }
    .collapsed-menu span{
      width: 0px;
      transition: width .2s ease;
    }
    .collapsed-menu i{
      transform: translateX(5px);
      transition: font-size .2s ease .2s, transform .2s ease .2s;
      vertical-align: middle;
      font-size: 22px;
    }
</style>
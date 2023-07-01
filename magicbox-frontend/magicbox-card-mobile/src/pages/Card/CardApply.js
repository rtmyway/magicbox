import React, { Component } from 'react';
import {Row, Col, Form, Input, Select, Button, Table, Popover, DatePicker, Modal, message, Alert} from 'antd';
import PageHeaderWrapper from '@/components/PageHeaderWrapper';
import checkCommon from '../../commons/CheckCommon'
import {add, load} from '../../services/apply'
import moment from 'moment';
import styles from './CardGlobal.less';


const FormItem = Form.Item;
const {Option } = Select;
const confirm = Modal.confirm;
@Form.create()
export default class CardApply extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      dataObj: {
      },
      applyObj: {
        phone: '',
        realName: '',
        expectedDateMoment: moment(new Date()),
        expectedDate: moment(new Date()).format('YYYY-MM-DD'),
        addressInfo: '',
        description: '',
      },

    };
  }

  //render前执行
  componentWillMount() {
    let dataObj = this.state.dataObj;
    let carryObj = this.props.location.state;
    dataObj = {...carryObj};

    this.setState({
      dataObj: dataObj,
    });

    
  }

  componentDidMount(){

  }

  onInputHandle = (item, e) => {
    let applyObj = this.state.applyObj;
    if (item == 'description') {
      applyObj.description = e.target.value;
    } else if (item == 'phone') {
      applyObj.phone = e.target.value;
    } else if (item == 'realName') {
      applyObj.realName = e.target.value;
    } else if (item == 'addressInfo') {
      applyObj.addressInfo = e.target.value;
    }
    this.setState({ applyObj: applyObj });
  }

  onDateChange = (date, dateString) => {
    let applyObj = this.state.applyObj;
    applyObj.expectedDate = dateString;
    applyObj.expectedDateMoment = date;
    this.setState({ applyObj: applyObj });
  }

  onConfirmHandler = (e) => {
    e.preventDefault();
    let dataObj = this.state.dataObj;

    let applyObj = this.state.applyObj;
    if (checkCommon.isEmpty(applyObj.phone) 
      || checkCommon.isEmpty(applyObj.realName) 
      || checkCommon.isEmpty(applyObj.addressInfo)
      || checkCommon.isEmpty(applyObj.expectedDate)) {
      message.error('未输入手机号,收货人姓名,收获地址,期望发货日');
      return;
    }

    let reqParam = {
      cardNo: dataObj.cardPo.cardNo,
      cardPassword: dataObj.cardPo.cardPassword,
      phone: applyObj.phone,
      realName: applyObj.realName,
      addressInfo: applyObj.addressInfo,
      expectedDate: applyObj.expectedDate,
      description: applyObj.description,
    }

    add(reqParam, (response) => {
      load(reqParam, (response) => {
        if (response== undefined || response == null) {
          console.info('false');
        } else {
          let cardPo = response.data.cardPo;
          let eventItem = cardPo.currentEventItem;
  
          if (cardPo == null) {
            message.error('卡券状态异常，请联系卖家');
            return;
          }
  
          if (eventItem == 'INIT') {
            message.error('卡券状态异常，请联系卖家');
            return;
          }
  
          if (eventItem == 'SOLD') {
            this.props.history.push({pathname:'/consumer/card-apply',state: response.data});
          } else {
            this.props.history.push({pathname:'/consumer/card-info',state: response.data});
          }
        }
      });
    });

  }  


  render() {
    
    return (
      <PageHeaderWrapper title="">
        <Row>
          <Col offset={2} span={20}>
            <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="卡号">
              <Input disabled value={this.state.dataObj.cardPo.cardNo} />
            </FormItem>     
            <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="手机号">
              <Input placeholder="请输入手机号" onChange={(e) => this.onInputHandle('phone', e)} value={this.state.applyObj.phone} />
            </FormItem>
            <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="收货人">
              <Input placeholder="请输入收货人姓名" onChange={(e) => this.onInputHandle('realName', e)} value={this.state.applyObj.realName} />
            </FormItem>      
            <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="地址">
              <Input placeholder="请输入收货地址" onChange={(e) => this.onInputHandle('addressInfo', e)} value={this.state.applyObj.addressInfo} />
            </FormItem>   
            <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="期望发货日">
              <DatePicker  onChange={this.onDateChange}  placeholder="请选择期望发货日" value={this.state.applyObj.expectedDateMoment} />
            </FormItem>            
            <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="备注">
              <Input placeholder="请输入备注(可选)" onChange={(e) => this.onInputHandle('description', e)} value={this.state.applyObj.description} />
            </FormItem>   
          </Col>
        </Row>   
        <Row>
          <Col offset={2} span={20}>
            <Button style={{ marginLeft: '0px', width: '100%'}} type="primary" onClick={this.onConfirmHandler}>确认</Button>
          </Col>
        </Row>             
      </PageHeaderWrapper>
    );
  }
}





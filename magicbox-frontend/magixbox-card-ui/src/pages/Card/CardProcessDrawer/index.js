import React, { Component } from 'react';
import { Form, Input, Row, Col, Spin, Drawer, Button, message, DatePicker } from 'antd';
import checkCommon from '../../../commons/CheckCommon';
import moment from 'moment';
import styles from './index.less';
const FormItem = Form.Item;
const { TextArea } = Input;

export default class CardProcessAddDrawer extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      dataObj: {
        cardId: '',
        cardNo: '',
        cardPassword: '',
        eventItemCode: '',
        eventItemName: '',
        phone: '',
        realName: '',
        addressInfo: '',
        expectedDateMoment: moment(new Date()),
        expectedDate: moment(new Date()).format('YYYY-MM-DD'),
        description: '',
      },
    }
  }

  componentWillReceiveProps(nextProps) {
    let viewObj = nextProps.viewObj;

    let dataObj = this.state.dataObj;


    if (viewObj.visible && viewObj.changeable) {
      dataObj.cardId = viewObj.data.cardId,
      dataObj.cardNo = viewObj.data.cardNo,
      dataObj.cardPassword = viewObj.data.cardPassword,
      dataObj.eventItemCode = viewObj.data.eventItemCode,
      dataObj.eventItemName = viewObj.data.eventItemName,
      dataObj.phone = viewObj.data.phone,
      dataObj.realName = viewObj.data.realName,
      dataObj.addressInfo = viewObj.data.addressInfo,
      dataObj.expectedDate = viewObj.data.expectedDate,
      dataObj.description = viewObj.data.description;
    }
    this.setState({
      dataObj: dataObj,
    });

  }

  onInputHandle = (item, e) => {
    let dataObj = this.state.dataObj;
    if (item == 'description') {
      dataObj.description = e.target.value;
    } else if (item == 'phone') {
      dataObj.phone = e.target.value;
    } else if (item == 'realName') {
      dataObj.realName = e.target.value;
    } else if (item == 'addressInfo') {
      dataObj.addressInfo = e.target.value;
    }
    this.setState({ dataObj: dataObj });
  }

  onDateChange = (date, dateString) => {
    let dataObj = this.state.dataObj;
    dataObj.expectedDate = dateString;
    dataObj.expectedDateMoment = date;
    this.setState({ dataObj: dataObj });
  }

  onConfirmHandler = (e) => {
    e.preventDefault();
    let dataObj = this.state.dataObj;
    if (dataObj.eventItemCode == 'APPLY') {
      if (checkCommon.isEmpty(dataObj.phone) || checkCommon.isEmpty(dataObj.realName) || checkCommon.isEmpty(dataObj.addressInfo)) {
        message.error('未输入手机号,收货人姓名,收获地址');
        return;
      }
    }
    this.props.viewObj.onSubViewActionHandler(this.props.viewObj.key, 'CONFIRM', dataObj);
  }

  render() {
    let applyFormItems = [];
    if (this.state.dataObj.eventItemCode == 'APPLY') {
      // 申请流程
      let tmpFormItem = '';

      // 手机号
      tmpFormItem = <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="手机号">
        <Input placeholder="请输入手机号" onChange={(e) => this.onInputHandle('phone', e)} value={this.state.dataObj.phone} />
      </FormItem>;    
      applyFormItems.push(tmpFormItem);

      // 姓名
      tmpFormItem = <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="收货人姓名">
        <Input placeholder="请输入收货人姓名" onChange={(e) => this.onInputHandle('realName', e)} value={this.state.dataObj.realName} />
      </FormItem>;
      applyFormItems.push(tmpFormItem);

      // 地址
      tmpFormItem = <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="收货地址">
        <Input placeholder="请输入收货地址" onChange={(e) => this.onInputHandle('addressInfo', e)} value={this.state.dataObj.addressInfo} />
      </FormItem>;   
      applyFormItems.push(tmpFormItem);      

      // 期望发货日
      tmpFormItem = <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="期望发货日">
        <DatePicker  onChange={this.onDateChange}  placeholder="请选择期望发货日" value={this.state.dataObj.expectedDateMoment} />
      </FormItem>;   
      applyFormItems.push(tmpFormItem);          
    }

    return (
      <div>
        <Drawer
          title={this.props.viewObj.title}
          placement={'right'}
          width={800}
          maskClosable={true}
          mask={true}
          onClose={() => this.props.viewObj.onSubViewVisibleHandler(this.props.viewObj.key, false, {})}
          visible={this.props.viewObj.visible}>
          <Spin spinning={this.props.viewObj.spinning}>
            <Row>
              <Col span={24}>
                <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="卡号">
                  <Input disabled value={this.state.dataObj.cardNo} />
                </FormItem>                  
                <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="处理步骤">
                  <Input disabled value={this.state.dataObj.eventItemName} />
                </FormItem>
                {applyFormItems}
                <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="备注">
                  <TextArea placeholder="请输入备注" rows={5} onChange={(e) => this.onInputHandle('description', e)} value={this.state.dataObj.description} />
                </FormItem>                
              </Col>
            </Row>
            <Row style={{ marginLeft: '80px' }} gutter={24}>
              <Col span={2} className='ant-col-offset-2'>
                <Button style={{ marginLeft: '0px' }} type="primary" onClick={this.onConfirmHandler}>确认</Button>
              </Col>
            </Row>
          </Spin>
        </Drawer>
      </div>
    );
  }
}

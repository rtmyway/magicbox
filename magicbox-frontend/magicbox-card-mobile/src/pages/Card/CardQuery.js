import React, { Component } from 'react';
import {Row, Col, Form, Input, Select, Button, Table, Popover, Timeline, Modal, message, Alert} from 'antd';
import PageHeaderWrapper from '@/components/PageHeaderWrapper';
import ViewCommon from '../../commons/ViewCommon'
import {add, load} from '../../services/apply'
import moment from 'moment';
import styles from './CardGlobal.less';


const FormItem = Form.Item;
const {Option } = Select;
const confirm = Modal.confirm;
@Form.create()
export default class CardQuery extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      dataObj: {
        cardNo: '',
        cardPassword: '',
      },

    };
  }

  //render前执行
  componentWillMount() {
    
  }

  componentDidMount(){

  }

  onInputHandle = (item, e) => {
    let dataObj = this.state.dataObj;
    if (item == 'cardNo') {
      dataObj.cardNo = e.target.value;
    } else if (item == 'cardPassword') {
      dataObj.cardPassword = e.target.value;
    }
    this.setState({ dataObj: dataObj });
  }


  onConfirmHandler = (e) => {
    e.preventDefault();
    const reqParam = {...this.state.dataObj};

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
  }  


  render() {
    
    return (
      <PageHeaderWrapper title="">
        <Row>
          <Col className='ant-col-offset-2' span={18}>
            <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="卡号">
              <Input onChange={(e) => this.onInputHandle('cardNo', e)} placeholder="请输入卡号" value={this.state.dataObj.cardNo} />
            </FormItem>                  
            <FormItem labelCol={{ span: 5 }} wrapperCol={{ span: 15 }} label="密码">
              <Input onChange={(e) => this.onInputHandle('cardPassword', e)} placeholder="请输入六位密码" value={this.state.dataObj.cardPassword} />
            </FormItem>            
          </Col>
        </Row>
        <Row>
          <Col span={18} className='ant-col-offset-2'>
            <Button style={{ marginLeft: '0px', width: '100%'}} type="primary" onClick={this.onConfirmHandler}>查询</Button>
          </Col>
        </Row>        
      </PageHeaderWrapper>
    );
  }
}





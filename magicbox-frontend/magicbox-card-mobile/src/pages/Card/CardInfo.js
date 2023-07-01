import React, { Component } from 'react';
import {Row, Col, Form, Input, Select, Descriptions, Badge, Popover, Timeline, Modal, message, Alert} from 'antd';
import PageHeaderWrapper from '@/components/PageHeaderWrapper';
import ViewCommon from '../../commons/ViewCommon'
import {add, load} from '../../services/apply'
import moment from 'moment';
import styles from './CardGlobal.less';


const FormItem = Form.Item;
const {Option } = Select;
const confirm = Modal.confirm;
@Form.create()
export default class CardInfo extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      dataObj: {
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




  render() {
    let statusTextArray = [
      {key: 'INIT', text: '未销售'},
      {key: 'SOLD', text: '已售出'},
      {key: 'APPLY', text: '提货已申请'},
      {key: 'CONFIRM', text: '提货已确认'},
      {key: 'DELIVER', text: '已发货'},
      {key: 'FINISH', text: '已完成'},
    ];
    let currentKey = this.state.dataObj.cardPo.currentEventItem;

    let findIndex = statusTextArray.findIndex((item) => {
      return item.key == currentKey;
    });

    let statusDom = <Badge status="error" text="异常信息,请联系商家"/>;
    if (findIndex != -1) {
      statusDom = <Badge status="processing" text={statusTextArray[findIndex].text}/>;
    }


    let eventLogPoList = this.state.dataObj.eventLogPoList;
    findIndex = eventLogPoList.findIndex((item) => {
      return item.eventItem == currentKey;
    });
    let descriptionText = '';
    if (findIndex != -1) {
      descriptionText = eventLogPoList[findIndex].description;
    }

    
    return (
      <PageHeaderWrapper title="">
        <Row>
          <Col offset={2} span={20}>
              <Descriptions title="状态" bordered size={'small'}>
                <Descriptions.Item label="卡号：">{this.state.dataObj.cardPo.cardNo}</Descriptions.Item>
                <Descriptions.Item label="状态：">{statusDom}</Descriptions.Item>
                <Descriptions.Item label="备注">{descriptionText}</Descriptions.Item>
              </Descriptions>
          </Col>
        </Row>    
        <Row style={{marginTop: '20px'}}>
          <Col offset={2} span={20}>
              <Descriptions title="收货信息" bordered size={'small'}>
                <Descriptions.Item label="手机：">{this.state.dataObj.applyPo.phone}</Descriptions.Item>
                <Descriptions.Item label="姓名：">{this.state.dataObj.applyPo.realName}</Descriptions.Item>
                <Descriptions.Item label="送货日期：">{this.state.dataObj.applyPo.expectedDate}</Descriptions.Item>
                <Descriptions.Item label="地址">{this.state.dataObj.applyPo.addressInfo}</Descriptions.Item>
              </Descriptions>
          </Col>
        </Row>               
      </PageHeaderWrapper>
    );
  }
}





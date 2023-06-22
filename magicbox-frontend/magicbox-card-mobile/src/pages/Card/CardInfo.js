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
export default class CardInfo extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      dataObj: {
        cardNo: '',
        cardName: '', 
        cardPassword: '',
        applyPo: {},
        eventLogPoList: [],
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
        console.info('true');
      }
      
    });
  }  


  render() {
    
    return (
      <PageHeaderWrapper title="">
        <Row>
          
        </Row>        
      </PageHeaderWrapper>
    );
  }
}





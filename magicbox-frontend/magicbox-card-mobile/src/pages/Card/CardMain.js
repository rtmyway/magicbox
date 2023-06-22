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
export default class CardMain extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,


    };
  }

  //render前执行
  componentWillMount() {

    
  }

  componentDidMount(){

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





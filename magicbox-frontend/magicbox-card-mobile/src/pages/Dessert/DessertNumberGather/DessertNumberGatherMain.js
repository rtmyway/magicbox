import React, { Component } from 'react';
import {Row, Col, Form, Input, Select, Button, Table, Switch, Icon, message, Modal, Tag ,Card} from 'antd';
import PageHeaderWrapper from '@/components/PageHeaderWrapper';

import styles from './DessertNumberGather.less';


const FormItem = Form.Item;
const {Option } = Select;
const confirm = Modal.confirm;
@Form.create()
export default class DessertNumberGatherMain extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loading : false,
      inputStr: 0,
      needSum: 0,
      numArray : [],       
      resultArray: [],    
    };
  }

  //render前执行
  componentWillMount() {

  }

  componentDidMount(){

  }

  doCalc = () => {
    let numArray = this.state.numArray.filter((item)=>{
      return item.check;
    });
    let len = Math.pow(2,numArray.length);
    let resultArray = [];


    let s = 0;
    let currentIndex;
    for (var i = 0; i < len; i++) {
      s = 0;
      currentIndex = i.toString(2);

      let numIndex = 0;
      for (var j = currentIndex.length -1; j >= 0; j--) {
        if (currentIndex[j] == 1) {
          s += numArray[numIndex].value;
        }
        numIndex++;
      }

      numIndex = 0;
      if (s == this.state.needSum) {
        let m = '';
        for (var j = currentIndex.length -1; j >= 0; j--) {
          if (currentIndex[j] == 1) {
            m += numArray[numIndex].value + ',';
          }
          numIndex++;
        }
        resultArray.push(m);
      }
    }
    this.setState({resultArray: resultArray});
  }

  render() {
    let totalNum = this.state.numArray.reduce((acc, current) => acc + current.value, 0);

    let titleDom = <div>
      <span>数字总和({totalNum})</span>
      <span onClick={()=>{

        }} style={{marginLeft:'20px',cursor:'pointer'}}>
      <input style={{marginLeft:'20px',}} placeholder='请输入数字' value={this.state.inputStr} onChange={(e)=>{
        let v = 0;
        v= parseInt(e.target.value); 
        if (!isNaN(v)) {
          this.setState({inputStr: v});
        }
      }}></input>    
      <Button style={{marginLeft:'20px'}} type='primary' onClick={()=>{
        let array = this.state.numArray;
        let numItem = {check:true, value: this.state.inputStr};
        array.push(numItem);
        this.setState({numArray: array});
      }}>添加</Button>
      </span>
    </div>;


    let numTagDom = this.state.numArray.map((item, index) => {
      return <div key={index} style={{marginTop:'15px'}}>
        <Switch size='small' checkedChildren={<Icon type="check" />} unCheckedChildren={<Icon type="close" />}
          checked={item.check}
          onChange={(v)=>{
            let array = this.state.numArray;
            array[index].check = v;
            this.setState({numArray: array});
          }}
        />
        <Tag style={{width:'200'}} key={index} style={{marginLeft:'10px'}} color='grey'>{item.value} </Tag>
        <Button size='small' type='danger' onClick={()=>{
          let array = this.state.numArray;
          array.splice(index,1);
          this.setState({numArray: array});
        }}>删除</Button>
      </div>;
    });

    let resultDom = this.state.resultArray.map((item, index)=>{
      return <p key={item}>{item}</p>
    });

    return (
    <PageHeaderWrapper title="">
    <Card title={titleDom}>
      {numTagDom}
    </Card>
    <Card bordered title={<div>
      <input style={{marginLeft:'20px',}} placeholder='请输入预计总和' value={this.state.needSum} onChange={(e)=>{
        let v = 0;
        v= parseInt(e.target.value); 
        if (!isNaN(v)) {
          this.setState({needSum: v});
        }
      }}></input>    
      <Button style={{marginLeft:'50px'}} size='small' type='primary' onClick={()=>{
        this.doCalc();
        }}>开始计算</Button>
      </div>}>
      <p>找到匹配结果{this.state.resultArray.length}个</p>
        {resultDom}
    </Card>    
    </PageHeaderWrapper>
    );
  }
}

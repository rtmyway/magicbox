import React, { Component } from 'react';
import {Row, Col, Form, Input, Select, Button, Table, Popover, Timeline, Modal, message, Alert} from 'antd';
import PageHeaderWrapper from '@/components/PageHeaderWrapper';
import ViewCommon from '../../commons/ViewCommon'
import {generate, update, remove, listPage} from '../../services/card'
import {add, process, load} from '../../services/apply'
import CardProcessDrawer from './CardProcessDrawer';
import moment from 'moment';
import styles from './CardGlobal.less';


const FormItem = Form.Item;
const {Option } = Select;
const confirm = Modal.confirm;
@Form.create()
export default class DbmtBackupRestoreMain extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      data: [],
      searchValue: '',
      eventItem: '',
      pagination: {
        total: 0,
        current: 1,
        pageSize: 10,
      },   
      views: ViewCommon.createViews([
        {key: 'PROCESS', title: '处理', visible: false, changeable: false, onSubViewVisibleHandler: this.onSubViewVisibleHandler, onSubViewActionHandler: this.onSubViewActionHandler},
      ]),                 
    };
  }

  //render前执行
  componentWillMount() {
    this.doLoadListPage(1, this.state.pagination.pageSize);
  }

  componentDidMount(){

  }

  /* 分页查询 */
  doLoadListPage = (pageNum, pageSize) => {
    const reqParam = {
      pageNum : pageNum,
      pageSize : pageSize,
      params: {searchValue: this.state.searchValue, eventItem: this.state.eventItem},
    };
    // 加载状态=>加载中
    this.setState({loading: true,});
    
    listPage(reqParam, (response) => {
      if (response != undefined && response != null) {
        let res = response.data;
        let list = res.list;

        let pageInfo = {
          total: res.total,
          current: res.pageNum,
          pageSize: res.pageSize,
        };
        for (let i = 0; i < list.length; i++) {
          list[i].key = list[i].id;
          list[i].serialNo = (res.pageNum - 1) * (res.pageSize) + i + 1;

        }
        this.setState({
          data: list,
          pagination: pageInfo
        });
      }
      // 加载状态=>完成
      this.setState({loading: false,});
    });
  }

  /* 分页查询 */
  doGenerate = (cnt) => {
    const reqParam = {
      cnt : cnt,
    };
    // 加载状态=>加载中
    this.setState({loading: true,});
    
    generate(reqParam, (response) => {
      this.doLoadListPage(1, this.state.pagination.pageSize);
    });
  }  


  //分页事件触发
  pageChangeHandler = (pagination, filtersArg, sorter) => {
    let pageObj = this.state.pagination;
    pageObj.current = pagination.current;
    pageObj.pageSize = pagination.pageSize;
    this.setState({pagination: pageObj,});
    this.doLoadListPage(pagination.current, pagination.pageSize);
  }

  //查询单击事件
  pageSearchHandler = (e) => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      let searchValue = values.searchValue == undefined ? '' : values.searchValue;
      let eventItem = values.eventItem == undefined ? '' : values.eventItem;
      this.setState({
        searchValue: searchValue,
        eventItem: eventItem,
      }, ()=>{this.doLoadListPage(1, this.state.pagination.pageSize);});
    });
  }


  /**
   * 子视图的显示/隐藏
   * @param viewKey 视图key
   * @param visible true:显示 false:隐藏
   * @param data 传递给子视图需要的数据
   */
  onSubViewVisibleHandler = (viewKey, visible, data) => {
    let tmpViews = this.state.views;
    for (let i = 0; i < tmpViews.length; i++) {
      if (viewKey == tmpViews[i].key) {
        tmpViews[i].visible = visible;
        tmpViews[i].data = data;
        tmpViews[i].changeable = true;
        this.setState({views: tmpViews});
        break;;
      }
    }
  }  

  /**
   * 子视图上所有的事件
   * @param viewKey 视图key
   * @param actionKey 事件key
   * @param data 事件需要的数据
   */
  onSubViewActionHandler = (viewKey, actionKey, data) => {
    let tmpViews = this.state.views;
    let index = -1;
    for (let i = 0; i < tmpViews.length; i++) {
      if (viewKey == tmpViews[i].key) {
        index = i;
        break;
      }
    }

    if (index == -1) {
      return;
    }

    tmpViews[index].changeable = false;
    tmpViews[index].spinning = true;
    this.setState({ views: tmpViews});

    // 新增
    if (viewKey == 'PROCESS') {
      const reqParam = {...data};
      process(reqParam, (response) => {
        let that = this;
        setTimeout(function () {
          message.success(`处理完成`);   
          that.doLoadListPage(1, that.state.pagination.pageSize);

          // 加载状态=>完成
          tmpViews[index].spinning = false;
          tmpViews[index].visible = false;
          that.setState({ views: tmpViews, });
        }, 1000);
      });
    }
  }  



  /*发起删除*/
  doRemove = (row) => {
    let that = this;
    confirm({
      title: `确定要删除 【${row.cardPo.cardNo}】 吗?`,
      content: '删除后无法恢复,请谨慎操作',
      okText: '删除',
      okType: 'danger',
      cancelText: '取消',
      onOk() {
        const reqParam = {
          id: row.cardPo.id,
        };
        // 加载状态=>完成
        that.setState({ loading: true, });
        remove(reqParam, (response) => {
            setTimeout(function () {
              message.success(`已删除`);   
              that.doLoadListPage(1, that.state.pagination.pageSize);
              that.setState({ loading: false, });
            }, 1000);            
        });        
        
      },
      onCancel() {

      },
    });
  }  

  getEventItemName = (code) => {
    let name = '';
    if (code == 'INIT') {
      name = '创建卡券';
    } else if (code == 'SOLD') {
      name = '售出卡券';
    } else if (code == 'APPLY') {
      name = '申请提货';
    } else if (code == 'CONFIRM') {
      name = '确认提货';
    } else if (code == 'DELIVER') {
      name = '开始配送';
    } else if (code == 'FINISH') {
      name = '全部完成';
    }
    return name;
  }


  render() {
    const columns = [
      {
        key : 1,
        title: '编号',
        width: '80px',
        dataIndex: 'serialNo',
      },{
        key : 2,
        title: '卡号和密码',
        width: '300px',
        render: (text, row, index) => {
          return <div>
            <p>{`卡号：${row.cardPo.cardNo}`} {`    密码：${row.cardPo.cardPassword}`}</p>
          </div>
        },
      },{
        key : 4,
        title: '状态',
        width: '500px',
        render: (text, row, index) => {
          let eventItemArray = ['INIT', 'SOLD', 'APPLY', 'CONFIRM', 'DELIVER', 'FINISH'];
          let isNext = false;
          let timelineItemList = eventItemArray.map((item, index)=>{
            let eventName = this.getEventItemName(item);
            
            let findIndex = row.eventLogPoList.findIndex((logItem)=>{
              return logItem.eventItem == item;
            });

            
            let timeStr = '';
            if (findIndex >= 0) {
              let timeStr = moment(row.eventLogPoList[findIndex].createdAt).format('YYYY-MM-DD HH:mm:ss');
              let description = row.eventLogPoList[findIndex].description;
              return <Timeline.Item key={index} color='green'>
                <span style={{color: 'green'}}>{timeStr}</span>
                <span style={{color: 'green', marginLeft: '10px'}}>{eventName}</span>
                <Popover content={description} title={eventName} trigger="hover">
                  <a style={{color:'grey', marginLeft: '20px'}}>查看备注</a>
                </Popover>
              </Timeline.Item>;
            } else {
              let processDom = '';
              if (isNext == false) {
                let cardPo = row.cardPo;
                let reqData = {
                  cardId: cardPo.id,
                  cardNo: cardPo.cardNo,
                  cardPassword: cardPo.cardPassword,
                  eventItemCode: item,
                  eventItemName: eventName,
                  description: '',
                };
                processDom = <a onClick={()=>{this.onSubViewVisibleHandler("PROCESS", true, reqData);}}>处理</a>;
                isNext = true;
              }
              
              return <Timeline.Item key={index} color='grey'><span>{timeStr}</span><span style={{color: 'grey', marginLeft: '10px'}}>{eventName}</span><span style={{marginLeft: '10px'}}>{processDom}</span></Timeline.Item>
            }
          });


          let timelineDom =  <Timeline>
            {timelineItemList}
          </Timeline>


          return <div>
            {timelineDom}
          </div>
        },
      },{
        key : 7,
        title: '提货信息',
        render: (text, row, index) => {
          let applyDom = '';
          if (row.applyPo != null) {
            applyDom = <Alert
              message={<span style={{fontWeight:'BOLD'}}>提货信息</span>}
              description={<div style={{color:'grey', marginTop: '20px'}}>
                <p>{`姓名：${row.applyPo.realName}`}</p>
                <p>{`手机号：${row.applyPo.phone}`}</p>
                <p>{`地址：${row.applyPo.addressInfo}`}</p>
                <p>{`期望发货日：${row.applyPo.expectedDate}`}</p>
              </div>}
              type="info"
              showIcon
            />
          }



          return <div>{applyDom}</div>
        },
      },{
        key : 9,
        title: '操作',
        render: (text, row, index) => {
          return <div>
            <a style={{fontWeight:'bold', marginLeft: '30px', color: 'red'}} onClick={()=>{this.doRemove(row);}}>{'删除'}</a>
          </div>
        },
      },
    ];

    const pagination = {
      current: this.state.pagination.current,
      pageSize: this.state.pagination.pageSize,
      total: this.state.pagination.total,
      showQuickJumper: true,
      showSizeChanger: true,
      pageSizeOptions: ['10', '20', '50'],
    };   
    const { getFieldDecorator } = this.props.form;
    return (
    <PageHeaderWrapper title="">
      <div className={styles.tableListForm}>
        <Form  onSubmit={this.pageSearchHandler} layout="inline">
          <Row gutter={24}>
            <Col span={4}>
              <FormItem label="检索内容:">
                {getFieldDecorator('searchValue')(
                  <Input placeholder="检索 卡号|提货手机号|提货人姓名|提货人地址" />
                )}
              </FormItem>
            </Col>       
            <Col span={4}>
              <FormItem label="状态:">
                {getFieldDecorator('eventItem', {initialValue:''})(
                  <Select style={{ width: '100%'}} >
                    <Option value="">全部</Option>
                    <Option value="INIT">初始化</Option>
                    <Option value="SOLD">已售出</Option>
                    <Option value="APPLY">提货已申请</Option>
                    <Option value="CONFIRM">提货已确认</Option>
                    <Option value="DELIVER">已发货</Option>
                    <Option value="FINISH">已完成</Option>
                  </Select>
                )}
              </FormItem>
            </Col>                 
            <Col className='ant-col-offset-10' span={2}>
              <FormItem>
                <Button type="default" htmlType="submit" style={{ width: '100%'}}>查询</Button>
              </FormItem>
            </Col>
            <Col className='ant-col-offset-0' span={2}>
              <FormItem>
                <Button onClick={()=>{this.doGenerate(1);}} type="primary" htmlType="button" style={{ width: '100%'}}>新增1张</Button>
              </FormItem>
            </Col>              
            <Col className='ant-col-offset-0' span={2}>
              <FormItem>
                <Button onClick={()=>{this.doGenerate(10);}} type="primary" htmlType="button" style={{ width: '100%'}}>新增10张</Button>
              </FormItem>
            </Col>            
          </Row>
          <Row gutter={24}>
            <Col span={24}>
              <Table
                bordered
                loading={this.state.loading}
                dataSource={this.state.data}
                columns={columns}
                pagination={pagination}
                onChange={this.pageChangeHandler}
              />
            </Col>
          </Row>
          <div>
            <CardProcessDrawer destroyOnClose={true} viewObj={this.state.views[0]}/>
          </div>               
        </Form>
      </div>
    </PageHeaderWrapper>
    );
  }
}





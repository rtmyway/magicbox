import React from 'react';
import {
  Router as DefaultRouter,
  Route,
  Switch,
  StaticRouter,
} from 'react-router-dom';
import dynamic from 'umi/dynamic';
import renderRoutes from 'umi/lib/renderRoutes';
import history from '@@/history';
import RendererWrapper0 from '/home/taozhen/repo/git_repo/github/magicbox/magicbox-frontend/magixbox-card-mobile/src/pages/.umi/LocaleWrapper.jsx';
import _dvaDynamic from 'dva/dynamic';

const Router = require('dva/router').routerRedux.ConnectedRouter;

const routes = [
  {
    path: '/',
    component: __IS_BROWSER
      ? _dvaDynamic({
          component: () => import('../../layouts/BlankLayout'),
          LoadingComponent: require('/home/taozhen/repo/git_repo/github/magicbox/magicbox-frontend/magixbox-card-mobile/src/components/PageLoading/index')
            .default,
        })
      : require('../../layouts/BlankLayout').default,
    Routes: [require('../Authorized').default],
    routes: [
      {
        path: '/consumer/card-query',
        name: 'card-query',
        icon: 'solution',
        component: __IS_BROWSER
          ? _dvaDynamic({
              component: () => import('../Card/CardQuery'),
              LoadingComponent: require('/home/taozhen/repo/git_repo/github/magicbox/magicbox-frontend/magixbox-card-mobile/src/components/PageLoading/index')
                .default,
            })
          : require('../Card/CardQuery').default,
        exact: true,
      },
      {
        path: '/consumer/card-info',
        name: 'card-info',
        icon: 'solution',
        component: __IS_BROWSER
          ? _dvaDynamic({
              component: () => import('../Card/CardInfo'),
              LoadingComponent: require('/home/taozhen/repo/git_repo/github/magicbox/magicbox-frontend/magixbox-card-mobile/src/components/PageLoading/index')
                .default,
            })
          : require('../Card/CardInfo').default,
        exact: true,
      },
      {
        component: () =>
          React.createElement(
            require('/home/taozhen/repo/git_repo/github/magicbox/magicbox-frontend/magixbox-card-mobile/node_modules/umi-build-dev/lib/plugins/404/NotFound.js')
              .default,
            { pagesPath: 'src/pages', hasRoutesInConfig: true },
          ),
      },
    ],
  },
  {
    component: () =>
      React.createElement(
        require('/home/taozhen/repo/git_repo/github/magicbox/magicbox-frontend/magixbox-card-mobile/node_modules/umi-build-dev/lib/plugins/404/NotFound.js')
          .default,
        { pagesPath: 'src/pages', hasRoutesInConfig: true },
      ),
  },
];
window.g_routes = routes;
const plugins = require('umi/_runtimePlugin');
plugins.applyForEach('patchRoutes', { initialValue: routes });

export { routes };

export default class RouterWrapper extends React.Component {
  unListen() {}

  constructor(props) {
    super(props);

    // route change handler
    function routeChangeHandler(location, action) {
      plugins.applyForEach('onRouteChange', {
        initialValue: {
          routes,
          location,
          action,
        },
      });
    }
    this.unListen = history.listen(routeChangeHandler);
    // dva 中 history.listen 会初始执行一次
    // 这里排除掉 dva 的场景，可以避免 onRouteChange 在启用 dva 后的初始加载时被多执行一次
    const isDva =
      history.listen
        .toString()
        .indexOf('callback(history.location, history.action)') > -1;
    if (!isDva) {
      routeChangeHandler(history.location);
    }
  }

  componentWillUnmount() {
    this.unListen();
  }

  render() {
    const props = this.props || {};
    return (
      <RendererWrapper0>
        <Router history={history}>{renderRoutes(routes, props)}</Router>
      </RendererWrapper0>
    );
  }
}

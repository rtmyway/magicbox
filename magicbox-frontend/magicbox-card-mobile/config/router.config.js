export default [
  {
    path: '/',
    component: '../layouts/BlankLayout',
    Routes: ['src/pages/Authorized'],
    routes: [
      {
        path: '/consumer/card-query',
        name: 'card-query',
        icon: 'solution',
        component: './Card/CardQuery',
      },    
      {
        path: '/consumer/card-info',
        name: 'card-info',
        icon: 'solution',
        component: './Card/CardInfo',
      },                                                     
    ],
  },

];

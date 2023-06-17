export default [
  {
    path: '/user/login',
    component: '../layouts/UserLayout',
    Routes: ['src/pages/Authorized'],
    routes: [
      {path: '/user/login', component: './User/Login'},
    ],
  },
  {
    path: '/',
    component: '../layouts/BasicLayout',
    Routes: ['src/pages/Authorized'],
    routes: [
      {
        path: '/mini/card-main',
        name: 'card-main',
        icon: 'solution',
        component: './Card/CardMain',
      },          
      {
        path: '/mini/number-gather',
        name: 'number-gather',
        icon: 'solution',
        component: './Dessert/DessertNumberGather/DessertNumberGatherMain',
      },                                         
    ],
  },

];

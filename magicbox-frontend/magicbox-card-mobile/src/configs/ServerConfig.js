//服务器三环境模式(1:dev, 2:beta, 3:prd)
const serverEnv = SERVER_ENV;

//card服务器地址
const cardServer = [
	{tag: 'CARD', env: 'DEV', host: 'http://localhost:8080', version: '1.0.0'},
	{tag: 'CARD', env: 'BETA', host: 'http://taozhen.tpddns.cn:25202', version: '1.0.0'},
	{tag: 'CARD', env: 'PRD', host: '/card-server', version: '1.0.0'},
];


export default {
	cardServer: cardServer[serverEnv - 1],
}
// Composables
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: () => import('@/layouts/default/Default.vue'),
    children: [
      {
        path: '',
        redirect: '/home'
      },
      {
        path: '/home',
        name: 'Home',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import(/* webpackChunkName: "home" */ '@/views/MainPage.vue'),
      },
      {
        path: "/signup",
        name: "Signup",
        component: () => import(/* webpackChunkName: "home" */ '@/views/SignUpView.vue'),
      },
      {
        path: '/products',
        name: 'product_list',
        component: () => import('@/views/ProductList.vue'),
        props: true
      },
      {
        path: '/subscribe/:id/I',
        name: 'subscribeI',
        component: () => import('@/views/SubscribeI.vue'),
        props: true
      },
      {
        path: '/subscribe/:id/D',
        name: 'subscribeD',
        component: () => import('@/views/SubscribeD.vue'),
        props: true
      },
      {
        path: '/subscribe/:id/IforShow',
        name: 'subscribeIforShow',
        component: () => import('@/views/SubscribeIforShow.vue'),
        props: true
      },
      {
        path: '/subscribe/:id/DforShow',
        name: 'subscribeDforShow',
        component: () => import('@/views/SubscribeDforShow.vue'),
        props: true
      },
      {
        path: '/recommend',
        name: 'recommended_product_list',
        component: () => import('@/views/RecommendedProduct.vue'),
        props: true
      },
      {
        path: '/mypage',
        name: 'Mypage',
        component: () => import('@/views/MyPage.vue')
      },
      {
        path: '/profile/edit',
        name: 'profile',
        component: () => import('@/views/UserProfileEdit.vue')
      },
      {
        path: '/security',
        name: 'security',
        component: () => import('@/views/SecuritySetting.vue')
      },
      {
        path: '/login',
        name: 'login',
        component: () => import('@/views/LoginView.vue')
      },
      {
        path: '/securitysetting',
        name: 'securitySetting',
        component: () => import('@/views/ForSecuritySetting.vue')
      },
      {
        path: '/security',
        name: 'security',
        component: () => import('@/views/SecurityMain.vue')
      },
      {
        path: '/changePW',
        name: 'changepassword',
        component: () => import('@/views/ForPasswordChange.vue')
      },
      {
        path: '/portfolio',
        name: 'portfolio',
        component: () => import('@/views/Portfolio.vue')
      },
      {
        path: '/portfoliodays',
        name: 'portfoliodays',
        component: () => import('@/views/PortfolioDays.vue')
      },
      {
        path: '/portfoliodibs',
        name: 'portfoliodibs',
        component: () => import('@/views/Portfoliodibs.vue')
      },
      {
        path: '/chartportfolio',
        name: 'chartportfolio',
        component: () => import('@/views/ChartPortfolio.vue')
      },
      {
        path: '/test',
        name: 'test',
        component: () => import('@/views/Test'),
      },
      {
        path: '/leave',
        name: 'leave',
        component: () => import('@/views/UserLeave'),
      },
      {
        path: '/error',
        name: 'error',
        component: () => import('@/views/Error'),
      }
    ]
  },
  {
    path: '/success',
    name: 'success',
    component: () => import('@/views/SubscribeSuccess'),
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
})

export default router

// Composables
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: () => import('@/layouts/default/Default.vue'),
    children: [
      {
        path: '',
        name: 'Home',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import(/* webpackChunkName: "home" */ '@/views/MainPage.vue'),
      },
      {
        path: 'home',
        name: 'Home',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import(/* webpackChunkName: "home" */ '@/views/MainPage.vue'),
      },


      {
        path: "header",
        name: "Header",
        component: () => import(/* webpackChunkName: "home" */ '@/views/Header.vue'),
      },
      {
        path: "signup",
        name: "Signup",
        component: () => import(/* webpackChunkName: "home" */ '@/views/SignUpView.vue'),
      },
      {
        path: "afterl",
        name: "AfterLogin",
        component: () => import(/* webpackChunkName: "home" */ '@/views/AfterLogin.vue'),
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
        path: '/success',
        name: 'success',
        component: () => import('@/views/SubscribeSuccess'),
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
        path: '/changePW',
        name: 'changepassword',
        component: () => import('@/views/ChangePassword.vue')
      },
      {
        path: '/portfolio',
        name: 'portfolio',
        component: () => import('@/views/Portfolio.vue')
      },
      
    ],
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
})

export default router

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
        component: () => import(/* webpackChunkName: "home" */ '@/views/Home.vue'),
      },

      {
        path: "login",
        name: "Login",
        component: () => import(/* webpackChunkName: "home" */ '@/views/LoginView.vue'),
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
        path: "logint",
        name: "LoginT",
        component: () => import(/* webpackChunkName: "home" */ '@/views/LoginViewTest.vue'),
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
      }

    ],
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
})

export default router

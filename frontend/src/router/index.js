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
        path: '/subscribe/:id/I',
        name: 'subscribeI',
        component: () => import('@/views/SubscribeI.vue')
      }
    ],
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
})

export default router

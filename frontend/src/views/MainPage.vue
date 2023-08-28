<template>
  <v-carousel show-arrows="hover">
  <v-carousel-item
    src = logo
    cover
  ></v-carousel-item>

  <v-carousel-item
    src="https://cdn.vuetifyjs.com/images/cards/hotel.jpg"
    cover
  ></v-carousel-item>

  <v-carousel-item
    src="https://cdn.vuetifyjs.com/images/cards/sunshine.jpg"
    cover
  ></v-carousel-item>
</v-carousel>
</template>

<script setup>
import logoImage from '@/assets/img/HaeDalLogo.png'
const logo = logoImage;
</script>

<style>

</style>
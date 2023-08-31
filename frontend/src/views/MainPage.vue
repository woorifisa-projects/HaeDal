<template>
  <v-carousel show-arrows="hover">
    <v-carousel-item> <img src="@/assets/img/HaeDalLogo.png">
    </v-carousel-item>

    <v-carousel-item src="https://cdn.vuetifyjs.com/images/cards/hotel.jpg" cover></v-carousel-item>

    <v-carousel-item src="https://cdn.vuetifyjs.com/images/cards/sunshine.jpg" cover></v-carousel-item>
  </v-carousel>

  <div style="height: 500px;">
    서비스 관련 설명 이미지 추가
  </div>
</template>

<script setup>

</script>

<style>
.v-carousel__controls {
  background: none !important;
}
</style>

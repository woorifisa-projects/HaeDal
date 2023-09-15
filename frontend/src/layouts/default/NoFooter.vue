<template>
    <v-app>
        <default-bar />

        <default-view />
    </v-app>
</template>

<script setup>
import DefaultBar from './AppBar.vue'
import DefaultView from './View.vue'

</script>
<style>
@import url(//fonts.googleapis.com/earlyaccess/notosanskr.css);

.notosanskr * {
    font-family: 'Noto Sans KR', sans-serif;
}

body {
    font-family: 'Noto Sans KR', sans-serif !important;
}
</style>
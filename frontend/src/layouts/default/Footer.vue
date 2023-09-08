<template>
  <footer>
    <v-footer flat class="bg-blue-lighten-4 text-center d-flex flex-column" style="padding-bottom: 10px;">
      <v-btn :icon="icon" variant="text" href="https://github.com/woorifisa-projects/HaeDal.git">
      </v-btn>
      <strong>HaeDal</strong>
      <div style="margin:20px 0px">
        여러분의 자산정보 맞춤 상품을 추천해드리는 해달 프로젝트입니다.<br>

        <!-- Phasellus feugiat arcu sapien, et iaculis ipsum elementum sit amet. Mauris cursus commodo interdum. Praesent ut
        risus eget metus luctus accumsan id ultrices nunc. Sed at orci sed massa consectetur dignissim a sit amet dui.
        Duis
        commodo vitae velit et faucibus. Morbi vehicula lacinia malesuada. Nulla placerat augue vel ipsum ultrices, cursus
        iaculis dui sollicitudin. Vestibulum eu ipsum vel diam elementum tempor vel ut orci. Orci varius natoque penatibus
        et magnis dis parturient montes, nascetur ridiculus mus. -->
      </div>


      <v-divider></v-divider>

      <div>
        {{ new Date().getFullYear() }} — <strong>Woori FISA TEAM 4</strong>

      </div>
    </v-footer>
  </footer>
</template>


<script setup>
import { ref } from 'vue';
const icon = 'mdi-github';


</script>
<style>
v-footer {
  width: 100%;
  height: 60px;
  /* footer의 높이 */
  position: absolute;
}
</style>
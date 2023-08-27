<template>
   <v-card
      class="mx-auto"
      max-width="600"
    >
<div>
    비밀번호 변경
</div>
<p>
<label id="lb_now_pw" for="now_pw">현재 비밀번호</label>
<input type="password" name="now_pw" id="now_pw" maxlength="20" style="width:336px" title="현재 비밀번호 입력" onfocus="convertDiv('now_pw', 'none')" onblur="convertDiv('now_pw', 'block');showCapslockForNowPw(-1);" onkeyup="showCapslockForNowPw(1);" onkeypress="capslock(event);">
</p>
</v-card>
</template>

<script>
export default {

}
</script>

<style>

</style>
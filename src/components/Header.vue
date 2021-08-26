<template>
  <div>
    <div class="container mt-2" style="margin-left: 7px">
      <div class="row">
        <div class="col-8">
          <input
            class="form-control me-2"
            type="search"
            placeholder="搜 索"
            aria-label="Search"
            v-model="searchValue"
            @input="find"
          />
        </div>
        <div class="col-4">
          <header-pop-menu @logout="logout" v-if="user.id > 0" />
          <button
            v-else
            class="btn btn-outline-success"
            style="float: right"
            type="submit"
            @click.prevent="btnLogin"
          >
            登 录
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { defineComponent, onMounted, onUnmounted, reactive, ref } from "vue";
import HeaderPopMenu from "./widget/HeaderPopMenu.vue";
import "bootstrap/dist/css/bootstrap.min.css";
import store from "../store";
import { treeEmitter } from "./Tree.vue";
import mitt from "mitt";
export const HeaderEmitter = mitt();
export default defineComponent({
  name: "BookMHeader",
  props: {},
  components: {
    HeaderPopMenu,
  },
  setup(props, context) {
    const hasLogin = ref(false);
    const searchValue = ref();
    const user = reactive({
      id: 0,
      name: "",
      token: 0,
    });
    onMounted(() => {
      const data = JSON.parse(store.state.user);
      user.id = data.id;
      user.name = data.name;
      user.token = data.token;
      // alert(user.value.id)
    });
    const initLoginStatus = () => {
      const data = JSON.parse(store.state.user);
      user.id = data.id;
      user.name = data.name;
      user.token = data.token;
      hasLogin.value = true;
    };
    HeaderEmitter.on("initLoginStatus", initLoginStatus);
    // to app
    const btnLogin = () => {
      context.emit("btnLogin", true);
    };
    const logout =() => {
      user.id = 0;
      context.emit('hasLogout')
    }
    const find =() => {
      treeEmitter.emit('find',searchValue.value)
    }
    onUnmounted(() => {
      HeaderEmitter.off("initLoginStatus");
    });
    return { btnLogin, user, hasLogin,searchValue,logout,find };
  },
});
</script>
<style scoped></style>

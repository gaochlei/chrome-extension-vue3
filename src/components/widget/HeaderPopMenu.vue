<template>
  <div class="dropdown" style="float: right">
    <dropdown :name="name" ref="dropdown">
      <template #drop_item>
        <dropdown-item class="text-dark dropdown-item"
          ><router-link :to="`/create`">关于</router-link></dropdown-item
        >
        <dropdown-item class="text-dark dropdown-item" disabled
          ><a href="#" >管理账户</a></dropdown-item
        >
        <dropdown-item class="text-dark dropdown-item"
          ><a href="#" @click.prevent="logout">退出登录</a></dropdown-item
        >
      </template>
    </dropdown>
  </div>
</template>

<script>
import { defineComponent, onMounted, onUnmounted, ref } from "vue";
import Dropdown from "./Dropdown.vue";
import DropdownItem from "./DropdownItem.vue";
import "bootstrap/dist/css/bootstrap.min.css";
import store from "../../store";
import mitt from "mitt";
import axios from "axios";
export const popMenuEmitter = mitt();
export default defineComponent({
  name: "HeaderPopMenu",
  props: {},
  components: {
    Dropdown,
    DropdownItem,
  },
  setup(props, context) {
    const name = ref();
    // name.value = props.loginName;

    const logout = () => {
      const user = JSON.parse(store.state.user)
      axios.get(store.state.baseUrl + 'api/user/logout/' + user.token).then((resp) => {
        const data = resp.data
        if(data.success){
          store.commit('setUser','')
          store.commit('setDirList','')
          context.emit('logout')
        }
      })
    }

    onMounted(() => {
      const user = JSON.parse(store.state.user);
      name.value = user.name;
    });

    const initName = () => {
      const user = JSON.parse(store.state.user);
      name.value = user.name;
      // alert(name.value);
    };
    popMenuEmitter.on("initName", initName);
    onUnmounted(() => {
      popMenuEmitter.off("initName");
    });

    return { name,logout };
  },
});
</script>

<style scoped></style>

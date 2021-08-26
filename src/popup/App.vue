<template>
  <div>
    <book-m-header @hasLogout="hasLogout" @btnLogin="btnLogin" />
    <login v-if="isLogin" @loginOver="loginOver" />
    <tree v-if="bookmarkIsVisiable" :list="bookMarks" />
  </div>
</template>

<script>
import { defineComponent, onMounted, ref } from "vue";
import Tree, { treeEmitter } from "../components/Tree.vue";
import Login from "../components/Login.vue";
import BookMHeader from "../components/Header.vue";
import store from "../store";
import isEmpty from "../util/tool";
import axios from "axios";
export default defineComponent({
  name: "App",
  components: { Tree, Login, BookMHeader },
  setup(props, context) {
    const bookMarks = ref();
    const user = ref();
    const bookmarkIsVisiable = ref(false);
    const isLogin = ref(false);
    const btnLogin = (data) => {
      isLogin.value = data;
    };
    bookMarks.value = [];
    onMounted(() => {
      reqDir();
      if (!isEmpty(user.value.token)) {
        bookmarkIsVisiable.value = true;
      }
    });
    const loginOver = (data) => {
      isLogin.value = false;
      bookmarkIsVisiable.value = true;
      reqDir();
    };
    const reqDir = () => {
      const userStr = store.state.user;
      user.value = JSON.parse(userStr);
      if (user.value.id) {
        const req = new XMLHttpRequest();
        const baseUrl = "http://192.168.43.147:8083/api/dir/reqDirs/" + user.value.id;
        req.open("GET", baseUrl, true);
        req.setRequestHeader("Authorization", user.token);
        req.setRequestHeader("Accept", "application/json");
        req.setRequestHeader("Content-type", "application/json;charset=UTF-8");
        req.send();
        req.timeout = 5 * 1000
        // alert(JSON.stringify(reqData));
        req.onreadystatechange = function () {
          if (this.readyState === XMLHttpRequest.DONE && this.status === 200) {
            req.clearTimeout()
            const data = req.responseText;
            const resp = JSON.parse(data);
            const treeData = array2Tree(resp.content, 0);
            bookMarks.value = treeData
            // chrome.extension.getBackgroundPage().console.log("treeData", treeData);
            store.commit("setDirList", treeData);
            treeEmitter.emit("treeDataHasChanged");
          }else{
            chrome.extension.getBackgroundPage().console.log("timeout ===========timeout")
            alert('请求网络失败，无法连接网络，加载本地数据')
            treeEmitter.emit("treeDataHasChanged");
          }
        };
      }
    };

    const array2Tree = (arr, parentId) => {
      if (isEmpty(arr)) {
        return [];
      }
      const result = [];
      for (let i = 0; i < arr.length; i++) {
        const item = arr[i];
        item.isShow = false;
        item.isModify = false;

        if (item.parent == parentId) {
          result.push(item);

          const children = array2Tree(arr, item.id);
          if (!isEmpty(children)) {
            item.children = children;
          } else {
            item.children = [];
          }
        }
      }
      return result;
    };

    const modifyTreeData = (arr) => {
      if (isEmpty(arr)) {
        return [];
      }
      const result = [];
      chrome.extension.getBackgroundPage().console.log("arr", arr);
      for (let i = 0; i < arr.length; i++) {
        const data = arr[i];
        const item = {};
        item.id = data.id;
        item.user = data.user;
        item.sort = data.sort;
        item.parent = data.parent;
        item.name = data.name;
        item.websiteList = typeof data.websiteList == "undefined" ? [] : data.websiteList;
        item.children = searchChildren(arr, data.id);
        item.isModify = false;
        item.isShow = true;
        result.push(item);
      }

      const list = [];
      result.map((item) => {
        if (item.parent == 0) {
          list.push(item);
        }
      });
      return list;
    };

    const hasLogout = () => {
      bookmarkIsVisiable.value = false;
    };

    const searchChildren = (arr, parentId) => {
      const children = [];
      for (let i = 0; i < arr.length; i++) {
        if (arr[i].parent == parentId) {
          arr[i].isShow = true;
          arr[i].isModify = false;
          children.push(arr[i]);
        } else {
          continue;
        }
      }
      return children;
    };
    return { bookMarks, isLogin, btnLogin, loginOver, bookmarkIsVisiable, hasLogout };
  },
});
</script>

<style>
html {
  width: 400px;
  height: 400px;
}
</style>

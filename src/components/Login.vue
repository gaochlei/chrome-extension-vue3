<template>
  <div class="d-flex justify-content-center align-items-center loading-container">
    <div class="modal-dialog w-50 w-100" v-if="isVisible">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel">请 登 录</h5>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
            @click.prevent="hide"
          ></button>
        </div>
        <div class="modal-body">
          <form>
            <div class="d-flex flex-column bd-highlight mb-3">
              <label
                for="recipient-name"
                class="col-form-label align-self-start"
                style="margin-left: 0"
              >
                邮 箱
              </label>
              <validate-input
                type="text"
                class="form-control p-2"
                id="recipient-name"
                :rules="emailRules"
                placeholder="请输入邮箱"
                v-model="userR.user.email"
              />
            </div>

            <div class="d-flex flex-column bd-highlight mb-3">
              <label
                for="message-text"
                class="col-form-label align-self-start"
                style="margin-left: 0"
              >
                密 码
              </label>
              <validate-input
                type="password"
                class="form-control"
                id="recipient-name"
                :rules="passwordRules"
                placeholder="请输入密码"
                v-model="userR.user.password"
                @keyup.enter="loginFunc"
              />
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-info btn-sm"
            data-bs-dismiss="modal"
            @click="clickSignUp"
          >
            注 册
          </button>
          <button type="button" class="btn btn-primary btn-sm" @click="loginFunc" @keyup.enter="loginFunc">
            登 录
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { ref, defineComponent, reactive, onUnmounted } from "vue";
import ValidateInput from "./widget/ValidateInput.vue";
import store from "../store";
import md5 from "js-md5";
import { HeaderEmitter } from "./Header.vue";
import { popMenuEmitter } from "./widget/HeaderPopMenu.vue";
export default defineComponent({
  name: "Login",

  components: {
    ValidateInput,
  },

  setup: (props, context) => {
    const passwordRules = [{ type: "required", message: "密码不能为空" }];
    const emailRules = [
      { type: "required", message: "电子邮箱地址不能为空" },
      { type: "email", message: "请输入正确的邮箱" },
    ];
    // const data = true;
    const clickSignUp = () => {
      //通知父组件 点击了注册按钮，注册对话框打开
      // context.emit("clickSignUp", data);
      console.log("DDDDD===");
    };
    const isVisible = ref(true);
    const loadingVisible = ref(false);
    const hide = () => {
      isVisible.value = false;
      // context.emit("closeLogin", false);
    };

    const userR = reactive({
      user: {
        email: "",
        password: "",
      },
    });

    const loginFunc = () => {
      //
      // loadingVisible.value = true;
      console.log("user == ", userR.user);
      userR.user.password = md5(userR.user.password + "!@#QWERT");
      // const url = "http://192.168.43.49/api/user/login";
      const url = "api/user/login";
      axios
        .post(store.state.baseUrl + url, userR.user, { timeout: 30 * 1000 })
        .then((response) => {
          console.log("response == ", response);
          const data = response.data;
          if (data.success) {
            // createMessage("登录成功", "success");
            // console.log("login user == ", data.content);
            //告诉父组件 登录成功,关闭登录对话框
            // alert("SDGKKKKKKKKK");
            store.commit("setUser", data.content);
            // alert(chrome.storage)
            // if(chrome) chrome.storage.sync.set({ userya: data.content }, () => {});
            // appEmitter.emit('initLoginStatus')
            // alert(JSON.stringify(HeaderEmitter))
            HeaderEmitter.emit("initLoginStatus");
            isVisible.value = false;
            context.emit("loginOver", false);
            setTimeout(() => {
              popMenuEmitter.emit("initName");
              // treeEmitter.emit("initTreeData");
              //告诉website子组件去请求网站数据
              // emitter.emit("requestSiteList");
              // const userya = await getLocalStorageValue("userya");
            }, 300);
          } else {
            // createMessage(data.message, "error");
          }
          // setTimeout(() => {
          //   loadingVisible.value = false;
          // }, 1000);
        });
    };

    return {
      clickSignUp,
      isVisible,
      hide,
      userR,
      loginFunc,
      passwordRules,
      emailRules,
      loadingVisible,
    };
  },
});
</script>

<style scoped>
a {
  color: #42b983;
}

label {
  margin: 0 0.5em;
  font-weight: bold;
}

code {
  background-color: #eee;
  padding: 2px 4px;
  border-radius: 4px;
  color: #304455;
}
</style>

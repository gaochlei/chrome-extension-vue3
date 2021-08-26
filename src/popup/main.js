import { createApp } from "vue"; // CHANGE
import App from "./App.vue";
import axios from "axios";
import store from "../store";
createApp(App).use(store).mount("#app");

// axios.defaults.baseURL = "http://192.168.43.147:8083/";

/**
 * axios拦截器
 */
axios.interceptors.request.use(
  function(config) {
    const user = JSON.parse(store.state.user);
    // alert(config.url);
    if (config.url != "api/user/login") {
    //   alert(user.token);
      const token = user.token;
      //   if (Tool.isNotEmpty(token)) {
      //     config.headers.token = token;
      //     console.log("请求headers增加token:", token);
      //   }
      config.headers.token = token;
      return config;
    } else {
      // alert(user.token);
      // const token = user.token;
      //   if (Tool.isNotEmpty(token)) {
      //     config.headers.token = token;
      //     console.log("请求headers增加token:", token);
      //   }
      // config.headers.token = token;
      return config;
    }
  }
  //   error => {
  //     // createMessage("请求有误！", "error");
  //     console.log("请求错误：", error);
  //     return Promise.reject(error);
  //   }
);
// axios.interceptors.response.use(
//   function(response) {
//     return response;
//   },
//   error => {
//     // createMessage('请求有误！', 'error')
//     const response = error.response;
//     const status = response.status;
//     if (status === 401) {
//       // 判断状态码是401 跳转到首页或登录页
//       console.log("未登录，跳到首页");
//       //   store.commit('setUser', {})
//       //   message.error('未登录或登录超时')
//       //   router.push('/')
//     }
//     return Promise.reject(error);
//   }
// );

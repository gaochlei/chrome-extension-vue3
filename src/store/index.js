import { createStore } from 'vuex'

const store =  createStore({
  state: {
    user: localStorage.getItem('user') || '',
    userId: localStorage.getItem('userId') || 0,
    token: localStorage.getItem('token') || 0,
    userName: localStorage.getItem('userName') || '',
    dirList: localStorage.getItem('dirList') || '',
    baseUrl: "http://192.168.43.147:8083/",
  },
  mutations: {
    setUser(state,user) {
      const userStr = JSON.stringify(user)
      state.user = userStr
      localStorage.setItem('user',userStr)
    },
    setUserId(state,userId) {
      state.userId = userId
      localStorage.setItem('userId',userId)
    },
    setToken(state,token) {
      state.token = token
      localStorage.setItem('token',token)
    },
    setUserName(state,userName) {
      state.userName = userName
      localStorage.setItem('userName',userName)
    },
    setDirList(state,dirList) {
      const list = JSON.stringify(dirList)
      state.dirList = list
      // alert(state.dirList)
      localStorage.setItem('dirList',list)
    }
  },
  actions: {
  },
  modules: {
  }
})

export default store

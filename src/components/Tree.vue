<template>
  <div class="mt-2" id="div_tree">
    <ul style="margin-left: 0px; padding-left: 20px">
      <li
        style="text-align: left; list-style: none"
        v-for="item in dirList"
        :key="item.id"
      >
        <img
          style="margin-right: 5px; width: 15px; height: 15px"
          v-if="item.isShow"
          src="../../public/icons/folder-open.png"
        />
        <img
          style="margin-right: 5px; width: 15px; height: 15px"
          v-else
          src="../../public/icons/folder-close.png"
        />
        <input
          v-if="item.isModify"
          :id="item.id"
          name="dir_item"
          href="#"
          @click="selected(item, $event)"
          style="cursor: pointer; font-size: 16px"
          v-model="item[keys.name]"
          autofocus="autofocus"
          @keyup.enter="menu_modifyFun(item, $event)"
        />
        <span
          v-else
          :id="item.id"
          name="dir_item"
          href="#"
          @click="selected(item, $event)"
          style="cursor: pointer; border: none; font-size: 16px"
          v-html="search(item)"
        >
        </span>

        <ul style="margin-left: 0px; padding-left: 20px" v-if="item.isShow">
          <li
            style="text-align: left; list-style: none"
            v-for="webItem in item[keys.websiteList]"
            :key="webItem.id"
            :id="webItem.id"
          >
            <img
              style="margin-right: 2px; width: 15px; height: 15px"
              src="../../public/icons/p-plane.png"
            />
            <input
              v-if="webItem.isModify"
              :id="webItem.id"
              name="dir_item"
              href="#"
              style="cursor: pointer"
              v-model="webItem[keys.websiteName]"
              autofocus="autofocus"
              @keyup.enter="menu_website_modifyFun(webItem, $event)"
            />
            <a
              v-else
              :id="webItem.id"
              name="dir_item"
              href="#"
              @click.prevent="webItemClick(webItem, $event)"
              v-html="searchWebsite(webItem)"
            ></a>
          </li>
        </ul>
        <tree :list="item[keys.children]" :keys="keys" v-if="item.isShow"></tree>
      </li>
    </ul>
  </div>
</template>

<script>
import { defineComponent, onMounted, onUnmounted, ref, watch } from "vue";
import axios from "axios";
import store from "../store";
import isEmpty from "../util/tool";
import mitt from "mitt";
export const treeEmitter = mitt();
/**
 * @description save each recursion dirList.value
 */
const dataArr = [];
/**
 * @description save current selected bookmark item
 */
let currentFolder = {};
export default defineComponent({
  name: "Tree",
  props: {
    list: {
      type: Array,
      required: true,
    },
    keys: {
      type: Object,
      default: {
        name: "name",
        websiteList: "websiteList",
        websiteName: "websiteName",
        children: "children",
      },
    },
  },
  setup(props, context) {
    const dirList = ref();
    const keys = ref();
    dataArr.push(dirList);
    // alert(props.list)
    dirList.value = props.list;
    // alert(JSON.stringify(props.list))
    keys.value = props.keys;
    const searchitem = ref("");
    const search = (item) => {
      return item.name.replace(
        new RegExp(searchitem.value, "g"),
        '<span v-else id="' +
          item.id +
          '" name="dir_item" href="#" @click="selected(item,$event)" style="color:red" >' +
          searchitem.value +
          "</span>"
      );
    };

    const searchWebsite = (websiteItem) => {
      return websiteItem.websiteName.replace(
        new RegExp(searchitem.value, "g"),
        '<a v-else id="' +
          websiteItem.id +
          '" name="dir_item" href="#" @click="webItemClick(webItem,$event)" style="color:red" >' +
          searchitem.value +
          "</a>"
      );
    };

    const find = (findValue) => {
      for (let i = 0; i < dataArr.length; i++) {
        allOpen(dataArr[i].value)
      }
      if (typeof findValue == "undefined") {
        findValue = "";
      }
      searchitem.value = findValue;
      chrome.extension.getBackgroundPage().console.log("findValue== ", findValue);
    };

    const allOpen = (arr) => {
      arr.map((item) => {
        item.isShow = true
        if(item.children && item.children.length > 0){
          allOpen(item.children)
        }
      })
    };

    treeEmitter.on("find", find);

    const treeDataHasChanged = () => {
      const list = JSON.parse(store.state.dirList);
      dirList.value = list;
    };
    //come from APP.vue onMounted msg
    treeEmitter.on("treeDataHasChanged", treeDataHasChanged);

    onUnmounted(() => {
      treeEmitter.off("search");
      treeEmitter.off("treeDataHasChanged");
    });

    const selected = (item, e) => {
      item.isShow = !item.isShow;
      // alert(item.isShow)
      currentFolder = item;
      chrome.extension.getBackgroundPage().console.log("item", item);
    };
    const initTreeData = () => {
      dirList.value = JSON.parse(store.state.dirList);
    };
    //come from Login.vue msg
    treeEmitter.on("initTreeData", initTreeData);

    const addNewFolderInBookmark = (arr, newFolder) => {
      for (let i = 0; i < arr.length; i++) {
        const item = arr[i];
        if (item.id == newFolder.parent) {
          if (typeof item.children == "undefined") {
            item.children = [];
          }
          item.children.push(newFolder);
        } else {
          if (item.children && item.children.length > 0) {
            addNewFolderInBookmark(item.children, newFolder);
          } else {
            continue;
          }
        }
      }
      return arr;
    };

    const ModifyObjectByAttrValue = (
      arr,
      itemID,
      attributeName,
      targetValue,
      targetElseValue
    ) => {
      for (let i = 0; i < arr.length; i++) {
        const item = arr[i];
        //根据 id查到要修改的数据
        if (item.id == itemID) {
          if (attributeName == "children") {
            item[attributeName].push(targetValue);
          } else {
            item[attributeName] = targetValue;
          }
        } else {
          if (targetElseValue != null && typeof targetElseValue != "undefined") {
            item[attributeName] = targetElseValue;
          }
          continue;
        }
        // 如果有子集，递归
        if (item.children && item.children.length > 0) {
          ModifyObjectByAttrValue(
            item.children,
            itemID,
            attributeName,
            targetValue,
            targetElseValue
          );
        }
      }
      return arr;
    };
    /**
     * @description 修改的书签website
     */
    const modifyWebsiteByItemId = (arr, targetId, targetValue) => {
      for (let i = 0; i < arr.length; i++) {
        const item = arr[i];
        if (item.id == targetId) {
          item.isModify = targetValue;
        } else {
          item.isModify = false;
        }
      }
    };

    const modifyWebsite = (arr, targetId, targetValue) => {
      for (let i = 0; i < arr.length; i++) {
        if (arr[i].websiteList && arr[i].websiteList.length > 0) {
          modifyWebsiteByItemId(arr[i].websiteList, targetId, targetValue);
        } else {
          if (arr[i].children && arr[i].children.length > 0) {
            modifyWebsite(arr[i].children, targetId, targetValue);
          } else {
            continue;
          }
        }
      }
    };
    let deletedBookMark;
    let deletedItemList;
    const deleteObjectById = (arr, targetId) => {
      for (let i = 0; i < arr.length; i++) {
        if (arr[i].id == targetId) {
          // nofityDeletedDir(arr[i]);
          // reqDeleteDirs(arr[i]);
          deletedBookMark = arr[i];
          arr.splice(i, 1);
          i--;
          deletedItemList = arr;
        } else {
          if (arr[i].children && arr[i].children.length > 0) {
            deleteObjectById(arr[i].children, targetId);
          } else {
            continue;
          }
        }
      }
      // chrome.extension.getBackgroundPage().console.log("arr", arr);
    };
    const menu_modifyFun = (item, e) => {
      for (let i = 0; i < dataArr.length; i++) {
        // alert(JSON.stringify(dataArr[i].value))
        if (typeof dataArr[i].value != "undefined")
          ModifyObjectByAttrValue(dataArr[i].value, item.id, "isModify", false, false);
      }
      saveDirs(true, item.id, [item]);
    };

    /**
     *@description 请求后台删除书签目录
     @param deletedDir 要删除的书签目录
     */
    const reqDeleteDirs = (deletedDir) => {
      axios.post(store.state.baseUrl + "api/dir/deleteDirs", deletedDir).then((resp) => {
        const data = resp.data;
        if (data.success) {
          chrome.extension
            .getBackgroundPage()
            .console.log("deletedItemList", deletedItemList);
          // for (let i = 0; i < dataArr.length; i++) {}
          store.commit("setDirList", deletedItemList);
        }
      });
    };

    /**
     *@description 请求后台删除书签中网站
     @param deletedDir 要删除的书签网站
     */
    const reqDeleteWebsite = (deleteWebsites) => {
      axios
        .post(store.state.baseUrl + "api/website/delete", deleteWebsites)
        .then((resp) => {
          const data = resp.data;
          if (data.success) {
          } else {
            alert("删除失败");
          }
        });
    };

    const saveDirs = (modify, itemId, dirs) => {
      // alert(JSON.stringify(dirs));
      axios.post(store.state.baseUrl + "api/dir/add/", dirs).then((response) => {
        const data = response.data;
        if (data.success) {
          const list = JSON.parse(store.state.dirList);
          if (modify) {
            // alert(store.state.dirList)
            const list_store = modifyTreeDataForStore(list, dirs[0]);
            // alert(JSON.stringify(list_store));
            store.commit("setDirList", list_store);
          } else {
            const list = array2Tree(data.content, 0);
            store.commit("setDirList", list);
            // dirList.value = list;
            // for (let i = 0; i < dataArr.length; i++) {
            chrome.extension
              .getBackgroundPage()
              .console.log("dataArr[0].value === ", dataArr[0].value);
            // itemId == 100 当在初层上新建子文件时会出现新建成功但是子文件夹没有在UI上刷新出来
            if (itemId == 100) {
              for (let i = 0; i < dataArr.length; i++) {
                addFolder(dataArr[i].value, dirs[0]);
              }
            } else {
              addFolder(dataArr[0].value, dirs[0]);
            }
            // }
          }
        }
      });
    };

    const addFolder = (arr, folder) => {
      arr.map((data) => {
        if (data.id == folder.parent) {
          data.children.push(folder);
        } else {
          if (data.children && data.children.length > 0) {
            addFolder(data.children, folder);
          }
        }
      });
      return arr;
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

    const modifyTreeDataForStore = (arr, target) => {
      arr.map((item) => {
        if (item.id == target.id) {
          item.name = target.name;
        } else {
          if (item.children && item.children.length > 0) {
            modifyTreeDataForStore(item.children, target);
          }
        }
      });
      return arr;
    };
    //网址修改触发的事件
    const menu_website_modifyFun = (item, e) => {
      for (let i = 0; i < dataArr.length; i++) {
        modifyWebsite(dataArr[i].value, e.target.id, false);
      }
      reqModifyWebsite([item]);
    };

    const webItemClick = (webItem, e) => {
      chrome.tabs.create({ url: webItem.siteUrl });
    };
    /**
     * @description add new website
     * @param arr bookmark tree json data
     * @param currentWeb current web page(url,page title)
     * @param e current selected html item that to get e.target.id for parentID
     */
    const addNewWebsite = (arr, currentWeb, e) => {
      const myDate = new Date();
      const user = JSON.parse(store.state.user);
      const website = {
        id: myDate.getTime(),
        userId: user.id,
        parentId: e.target.id,
        websiteName: currentWeb.title,
        siteUrl: currentWeb.url,
      };
      addWebsiteReq(arr, website);
    };
    const addWebsiteReq = (arr, website) => {
      // isLoading.value = true;
      axios.post(store.state.baseUrl + "api/website/add", website).then((resp) => {
        const data = resp.data;
        if (data.success) {
          const list = JSON.parse(store.state.dirList);
          const list_store = addTreeItemByItemId(list, website);
          // alert(JSON.stringify(list_store))
          store.commit("setDirList", list_store);
          addTreeItemByItemId(arr, website);
        } else {
          if (!isEmpty(data.message)) {
            alert(data.message);
          } else {
            alert("请求网络失败");
          }
        }
      });
    };

    const addTreeItemByItemId = (arr, website) => {
      arr.map((item) => {
        if (item.id == website.parentId) {
          if (typeof item.websiteList == "undefined") {
            item.websiteList = [];
          }
          item.websiteList.push(website);
        } else {
          if (item.children && item.children.length > 0) {
            addTreeItemByItemId(item.children, website);
          }
        }
      });
      return arr;
    };

    let div_this;
    let div_f;
    window.oncontextmenu = (e) => {
      e.preventDefault();
      if (e.target.attributes["name"].nodeValue == "dir_item") {
        if (div_this == null) {
          const dirs = document.getElementsByName("dir_item");
          // 遍历所有的文件夹使其背景色为白色
          for (let i = 0; i < dirs.length; i++) {
            const item = dirs[i];
            if (item) {
              item.style.backgroundColor = "#FFFFFF";
            }
          }

          if (e.target) {
            e.target.style.backgroundColor = "#D2D2D2";
          }
          div_f = document.createElement("div");
          const div_menu_rename = document.createElement("div");
          div_menu_rename.innerHTML = "修改";
          const div_menu_del = document.createElement("div");
          div_menu_del.innerHTML = "删除";
          const div_menu_new_fold = document.createElement("div");
          div_menu_new_fold.innerHTML = "新建文件夹";
          const div_menu_fast_add = document.createElement("div");
          div_menu_fast_add.innerHTML = "添加当前网址";
          const left = "left:" + e.clientX + "px;";
          const top = "top:" + e.clientY + "px";
          div_f.style.cssText =
            "width:125px;height:130px;background:#FFFFFF;box-shadow:0 1px 1px #888,1px 0 1px #ccc;position:fixed;z-index:99999;" +
            left +
            top;
          div_menu_rename.style.cssText =
            "width:110px;height:25px;line-height:25px;padding:0 10px;margin-top:10px; font-size:14px;text-align:left;cursor:pointer";
          div_menu_del.style.cssText =
            "width:110px;height:25px;line-height:25px;padding:0 10px; font-size:14px;text-align:left;cursor:pointer";
          div_menu_new_fold.style.cssText =
            "width:110px;height:25px;line-height:25px;padding:0 10px; font-size:14px;text-align:left;cursor:pointer";
          div_menu_fast_add.style.cssText =
            "width:110px;height:25px;line-height:25px;padding:0 10px; font-size:14px;text-align:left;cursor:pointer";

          div_menu_new_fold.setAttribute("class", "item");
          div_menu_fast_add.setAttribute("class", "item");
          div_this = document.getElementById("div_tree");
          div_f.appendChild(div_menu_rename);
          div_f.appendChild(div_menu_del);
          if (e.target.nodeName == "SPAN") {
            div_f.appendChild(div_menu_new_fold);
            div_f.appendChild(div_menu_fast_add);
          } else {
            div_f.style.cssText =
              "width:125px;height:70px;background:#FFFFFF;box-shadow:0 1px 1px #888,1px 0 1px #ccc;position:fixed;z-index:99999;" +
              left +
              top;
          }
          div_this.appendChild(div_f);
          //添加当前页为书签
          div_menu_fast_add.onclick = () => {
            const currentWebsite = { url: "", title: "" };
            chrome.tabs.query({ active: true, lastFocusedWindow: true }, (tabs) => {
              currentWebsite.url = tabs[0].url;
              currentWebsite.title = tabs[0].title;
              // alert(currentWebsite.url)
              dataArr.every((itemArr) => {
                addNewWebsite(itemArr.value, currentWebsite, e);
              });
            });
          };

          //新建文件夹
          div_menu_new_fold.onclick = () => {
            // alert("xinjianwenjianjia");
            div_f.style.display = "none";
            div_this = null;
            div_f = null;
            const myDate = new Date();
            const user = JSON.parse(store.state.user);
            const children = {
              id: myDate.getTime(),
              user: user.id,
              parent: e.target.id,
              name: "新建文件夹",
              websiteList: [],
              isModify: true,
              isShow: true,
              children: [],
              sort: 1,
            };
            chrome.extension.getBackgroundPage().console.log("e.target", e.target);
            saveDirs(false, e.target.id, [children]);
          };
          if (e.target.id == 100) {
            div_menu_del.style.color = "#b2b2b2";
            div_menu_rename.style.color = "#b2b2b2";
          } else {
            div_menu_rename.setAttribute("class", "item");
            div_menu_del.setAttribute("class", "item");
            // 菜单-修改事件
            div_menu_rename.onclick = () => {
              if (e.target.nodeName == "SPAN") {
                for (let i = 0; i < dataArr.length; i++) {
                  ModifyObjectByAttrValue(
                    dataArr[i].value,
                    e.target.id,
                    "isModify",
                    true,
                    false
                  );
                }
              } else {
                for (let i = 0; i < dataArr.length; i++) {
                  modifyWebsite(dataArr[i].value, e.target.id, true);
                }
              }
            };
            //菜单-删除
            div_menu_del.onclick = () => {
              if (e.target.nodeName == "SPAN") {
                // alert(JSON.stringify(dataArr[0].value))
                for (let i = 0; i < dataArr.length; i++) {
                  deleteObjectById(dataArr[i].value, e.target.id);
                }
                reqDeleteDirs([deletedBookMark]);
              } else {
                for (let i = 0; i < dataArr.length; i++) {
                  deleteWebsiteById(dataArr[i].value, e.target.id);
                  if (deletedItem.id) {
                    chrome.extension
                      .getBackgroundPage()
                      .console.log("deletedItem==", deletedItem);
                    reqDeleteWebsite([deletedItem]);
                  }
                }
              }
            };
          }
        }
      }
    };
    window.onclick = function (e) {
      //用户触发click事件就可以关闭了，因为绑定在window上，按事件冒泡处理，不会影响菜单的功能
      div_f.style.display = "none";
      div_this = null;
      div_f = null;
    };
    /**
     * @description 根据 website id 递归查询删除 website
     * @param arr 书签递归结构数组数据
     * @param targetId 要删除的website id
     */
    let deletedItem = {};
    const deleteWebsiteById = (arr, targetId) => {
      for (let i = 0; i < arr.length; i++) {
        const item = arr[i];
        chrome.extension.getBackgroundPage().console.log("item===", item);
        if (item.websiteList && item.websiteList.length > 0) {
          for (let j = 0; j < item.websiteList.length; j++) {
            //找到了
            if (item.websiteList[j].id == targetId) {
              deletedItem = item.websiteList[j];
              //删除
              item.websiteList.splice(j, 1);
              j--;
            } else {
              continue;
            }
          }
          //item.websiteList为空，查看 children是否有内容，有则递归，无则 continue进行 下一次循环
        }
        if (item.children && item.children.length > 0) {
          deleteWebsiteById(item.children, targetId);
        } else {
          continue;
        }
      }
      return arr;
    };
    /**
     *@description 请求后台修改书签
     @param websites 要修改的书签
     */
    const reqModifyWebsite = (websites) => {
      if (websites.length > 0) {
        axios.post(store.state.baseUrl + "api/website/update", websites).then((resp) => {
          console.log("modify website resp == ", resp.data);
        });
      }
    };
    return {
      selected,
      dirList,
      keys,
      menu_modifyFun,
      menu_website_modifyFun,
      webItemClick,
      search,
      searchWebsite,
    };
  },
});
</script>

<style>
p {
  font-size: 20px;
}
/*当鼠标移动到当前标签上时，以下css属性才生效*/
.item:hover {
  background-color: darkgrey;
}
</style>

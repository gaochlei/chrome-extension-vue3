
/**
 * 空校验 null或""都返回true
 */
const isEmpty = obj => {
  if (typeof obj === "string") {
    return !obj || obj.replace(/\s+/g, "") === "";
  } else {
    return !obj || JSON.stringify(obj) === "{}" || obj.length === 0;
  }
};

export default isEmpty;

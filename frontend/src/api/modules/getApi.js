import instance from ".."

export const getApi = async(param) => {
    const {data} = await instance({
    method:"get",
    url: param.url,
    headers: {
        Authorization: `Bearer ${localStorage.getItem('accessToken')}`, // 토큰 포함
      },
});
return data;
}

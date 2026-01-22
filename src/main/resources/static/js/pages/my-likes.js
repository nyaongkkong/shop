$(function () {

  const token = localStorage.getItem('accessToken');

  if (!token) {
    alert('로그인이 필요합니다.');
    location.href = '/login';
    return;
  }

  $.ajax({
    url: '/api/products/me/likes',
    method: 'GET',
    headers: {
      Authorization: 'Bearer ' + token
    },
    success: function (res) {

      if (!res || !res.success) {
        alert('찜 목록 조회 실패');
        return;
      }

      renderLikes(res.data);
    },
    error: function () {
      alert('서버 오류');
    }
  });
});

function renderLikes(products) {

  const $grid = $('#likeGrid');
  $grid.empty();

  if (!products.length) {
    $grid.append(`<div style="color:#888;">찜한 상품이 없습니다.</div>`);
    return;
  }

  products.forEach(p => {

    const thumb = p.thumbnailUrl
      ? `<img src="${p.thumbnailUrl}" style="width:100%; height:100%; object-fit:cover;">`
      : `<span>NO IMAGE</span>`;

    const price = Number(p.price).toLocaleString();

    const html = `
      <div class="product-card">
        <div class="product-thumb">
          <div class="product-thumb-inner">${thumb}</div>
        </div>
        <div class="product-brand">${p.brandName}</div>
        <div class="product-name">${p.name}</div>
        <div class="product-price">
          <span>${price}</span>
          <span class="product-price-label">원</span>
        </div>
      </div>
    `;

    const $card = $(html);
    $card.on('click', function () {
      location.href = '/products/' + p.slug;
    });

    $grid.append($card);
  });
}
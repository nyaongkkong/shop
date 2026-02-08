$(function () {

  const q = getQueryParam('q') || '';
  let sort = getQueryParam('sort');

  if (!q.trim()) {
    $('#searchTitle').text('검색');
    renderEmpty('검색어를 입력해 주세요.');
    return;
  }

  // sort 없으면 인기순 기본 적용
    if (!sort) {

      const newUrl =
        '/search?q=' +
        encodeURIComponent(q) +
        '&sort=POPULAR';

      if (window.location.href !== newUrl) {
        location.replace(newUrl);
      }

      return;
    }

  $('#searchTitle').text("‘" + q + "’ 검색 결과");

  // 필터 버튼 active 처리
  setActiveSort(sort);

  // 검색 API 호출
  $.ajax({
    url: '/api/search',
    method: 'GET',
    data: {
      q: q,
      sort: sort
    },
    success: function (res) {
      if (!res || res.success !== true) {
        renderEmpty('검색 결과를 불러오지 못했습니다.');
        return;
      }

      renderBrands(res.data.brands || []);
      renderProducts(res.data.products || []);
    },
    error: function (xhr) {
      console.log('search api error:', xhr.status, xhr.responseText);
      renderEmpty(xhr?.responseJSON?.error?.message || '서버 오류가 발생했습니다.');
    }
  });

  // 필터 버튼 클릭 이벤트
  $('.filter-btn').on('click', function () {

    const newSort = $(this).data('sort');

    location.href =
      '/search?q=' +
      encodeURIComponent(q) +
      '&sort=' + newSort;
  });
});

/* =========================
   SORT UI
========================= */

function setActiveSort(sort) {

  $('.filter-btn').each(function () {

    const btnSort = $(this).data('sort');

    if (btnSort === sort) {
      $(this).addClass('active');
    } else {
      $(this).removeClass('active');
    }
  });
}

/* =========================
   BRAND RENDER
========================= */

function renderBrands(brands) {

  const $grid = $('#brandGrid');
  $grid.empty();

  if (!brands.length) {
    $grid.append(`<div style="color:#888; font-size:13px;">일치하는 브랜드가 없습니다.</div>`);
    return;
  }

  brands.forEach(b => {

    const thumb = b.logoUrl
      ? `<img src="${escapeHtml(b.logoUrl)}" alt="" style="width:100%; height:100%; object-fit:cover;">`
      : `<span>${escapeHtml(b.name || '')}</span>`;

    const cardHtml = `
      <div class="product-card">
        <div class="product-thumb">
          <div class="product-thumb-inner">${thumb}</div>
        </div>
        <div class="product-name">${escapeHtml(b.name || '')}</div>
        <div class="product-brand">바로가기</div>
      </div>
    `;

    const $card = $(cardHtml);

    $card.on('click', function () {
      location.href = '/brands/' + encodeURIComponent(b.slug);
    });

    $grid.append($card);
  });
}

/* =========================
   PRODUCT RENDER
========================= */

function renderProducts(products) {

  const $grid = $('#productGrid');
  $grid.empty();

  if (!products.length) {
    $grid.append(`<div style="color:#888; font-size:13px;">일치하는 상품이 없습니다.</div>`);
    return;
  }

  products.forEach(p => {

    const thumb = p.thumbnailUrl
      ? `<img src="${escapeHtml(p.thumbnailUrl)}" alt="" style="width:100%; height:100%; object-fit:cover;">`
      : `<span>NO IMAGE</span>`;

    const priceText = formatPrice(p.price);

    const cardHtml = `
      <div class="product-card">
        <div class="product-thumb">
          <div class="product-thumb-inner">${thumb}</div>
        </div>
        <div class="product-brand">${escapeHtml(p.brandName || '')}</div>
        <div class="product-name">${escapeHtml(p.name || '')}</div>
        <div class="product-price">
          <span>${priceText}</span>
          <span class="product-price-label">원</span>
        </div>
      </div>
    `;

    const $card = $(cardHtml);

    $card.on('click', function () {
      location.href = '/products/' + encodeURIComponent(p.slug);
    });

    $grid.append($card);
  });
}

/* =========================
   UTIL
========================= */

function renderEmpty(msg) {
  $('#brandGrid').html(`<div style="color:#888; font-size:13px;">${escapeHtml(msg)}</div>`);
  $('#productGrid').empty();
}

function getQueryParam(key) {
  const params = new URLSearchParams(window.location.search);
  return params.get(key);
}

function formatPrice(price) {
  const n = typeof price === 'number' ? price : Number(price);
  if (Number.isFinite(n)) return n.toLocaleString('ko-KR');
  return (price == null ? '' : String(price));
}

function escapeHtml(str) {
  if (str == null) return '';
  return String(str)
    .replaceAll('&', '&amp;')
    .replaceAll('<', '&lt;')
    .replaceAll('>', '&gt;')
    .replaceAll('"', '&quot;')
    .replaceAll("'", '&#039;');
}

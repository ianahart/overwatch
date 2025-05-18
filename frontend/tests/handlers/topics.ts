import { http, HttpResponse } from 'msw';
import { baseURL } from '../../src/util';
import { createTopicWithTags, getSpecificTopicsWithTags, getTopicWithTags } from '../mocks/dbActions';
import { ICreateTopicRequest, IGetTopicsWithTagsResponse } from '../../src/interfaces';
import { paginate } from '../utils';
import { db } from '../mocks/db';

export const topicHandlers = [
  http.get(`${baseURL}/topics/tags`, async ({ request }) => {
    const url = new URL(request.url);
    const query = url.searchParams.get('query');

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = 2;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 10;
    const numOfTags = 5;

    query !== null ? createTopicWithTags(totalElements, numOfTags, query) : createTopicWithTags(20, 5);
    const data = getSpecificTopicsWithTags(totalElements, query!);

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IGetTopicsWithTagsResponse>(
      {
        message: 'success',
        data: {
          items,
          page,
          pageSize,
          totalPages,
          direction,
          totalElements,
        },
      },
      { status: 200 }
    );
  }),

  http.post(`${baseURL}/topics`, async ({ request }) => {
    const body = (await request.json()) as ICreateTopicRequest;

    if (!body.description || !body.title) {
      return HttpResponse.json({ message: 'Please fill out all fields' }, { status: 400 });
    }

    return HttpResponse.json({ message: 'success' }, { status: 201 });
  }),
  http.get(`${baseURL}/topics/search`, async ({ request }) => {
    const url = new URL(request.url);
    const query = url.searchParams.get('query');

    if (query === 'test') {
      db.topic.create({ title: 'Test Topic' });
      return HttpResponse.json({
        message: 'success',
        data: db.topic.getAll(),
      });
    }
    return HttpResponse.json({
      message: 'success',
      data: [],
    });
  }),

  http.get(`${baseURL}/topics`, async ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = Number(url.searchParams.get('pageSize')) ?? 10;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 20;

    createTopicWithTags(20, 5);
    const data = getTopicWithTags(20);

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IGetTopicsWithTagsResponse>(
      {
        message: 'success',
        data: {
          items,
          page,
          pageSize,
          totalPages,
          direction,
          totalElements,
        },
      },
      { status: 200 }
    );
  }),

  http.get(`${baseURL}/topics/users/:userId`, async ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = Number(url.searchParams.get('pageSize')) ?? 10;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 20;

    createTopicWithTags(20, 5);
    const data = getTopicWithTags(20);

    const { page, totalPages, pageSize, direction, items } = paginate(pg, size, dir, data);

    return HttpResponse.json<IGetTopicsWithTagsResponse>(
      {
        message: 'success',
        data: {
          items,
          page,
          pageSize,
          totalPages,
          direction,
          totalElements,
        },
      },
      { status: 200 }
    );
  }),
];

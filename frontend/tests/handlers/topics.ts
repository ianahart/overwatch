import { http, HttpResponse } from 'msw';
import { baseURL } from '../../src/util';
import {
  createTopicWithTag,
  createTopicWithTags,
  getSpecificTopicsWithTags,
  getTopicWithTag,
  getTopicWithTags,
} from '../mocks/dbActions';
import {
  ICreateTopicRequest,
  IGetTopicResponse,
  IGetTopicsWithTagsResponse,
  IUpdateTopicRequest,
} from '../../src/interfaces';
import { paginate } from '../utils';
import { db } from '../mocks/db';

export const topicHandlers = [
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

  http.patch(`${baseURL}/topics/:topicId`, async ({ request }) => {
    const body = (await request.json()) as IUpdateTopicRequest;

    if (body.description.length > 250) {
      return HttpResponse.json({ message: 'Description must be between 1 and 250' }, { status: 400 });
    }

    return HttpResponse.json({ message: 'success' }, { status: 200 });
  }),

  http.get(`${baseURL}/topics/:topicId`, async ({ params }) => {
    if (!params.topicId) {
      return HttpResponse.json({ message: 'Missing topicId' }, { status: 400 });
    }

    const id = parseInt(params.topicId as string) as number;

    createTopicWithTag({ title: 'title', description: 'description', id });
    const topic = getTopicWithTag(id);

    if (!topic) {
      return HttpResponse.json({ message: 'Could not fetch topic with id ' + id }, { status: 400 });
    }
    return HttpResponse.json<IGetTopicResponse>(
      {
        message: 'success',
        data: topic,
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
  http.get(`${baseURL}/topics/users/:userId`, async ({ request }) => {
    const url = new URL(request.url);

    let pg = Number(url.searchParams.get('page')) ?? 1;
    const size = Number(url.searchParams.get('pageSize')) ?? 10;
    const dir = url.searchParams.get('direction') ?? 'next';
    const totalElements = 20;

    createTopicWithTags(20, 5);
    const data = getTopicWithTags(20);

    console.log(data);
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
